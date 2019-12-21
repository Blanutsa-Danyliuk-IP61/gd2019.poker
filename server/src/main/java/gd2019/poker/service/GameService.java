package gd2019.poker.service;

import gd2019.poker.dto.*;
import gd2019.poker.events.*;
import gd2019.poker.model.*;
import gd2019.poker.model.enums.BidType;
import gd2019.poker.model.enums.BlindType;
import gd2019.poker.model.enums.PlayerStatus;
import gd2019.poker.model.enums.TournamentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GameService {

    public static final int GAME_PLAYERS_QUANTITY = 3;
    public static final int DEFAULT_BALANCE = 1000;
    public static final int DEFAULT_SMALL_BLIND = 10;
    public static final int DEFAULT_BIG_BLIND = 20;

    private final SimpMessagingTemplate messagingTemplate;
    private final Repository repository;
    private final int eventDelayInSeconds;

    public GameService(Repository repository, SimpMessagingTemplate messagingTemplate,
                       @Value("${poker.event.delayInSeconds}") int eventDelayInSeconds) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
        this.eventDelayInSeconds = eventDelayInSeconds;
    }

    public void handleConnected(ConnectRequest request, String sessionId){
        UUID id = UUID.fromString(request.getId());
        Player player = repository.getPlayerByID(id);

        EventGroupDTO eventGroup = new EventGroupDTO(eventDelayInSeconds);
        EventGroupDTO connectedEventGroup = new EventGroupDTO(eventDelayInSeconds);

        if (player != null && player.getCurrentTournament() != null) {
            player.setActive(true);

            eventGroup.addEvent(ReconnectedEventDTO.builder()
                    .reconnectedPlayer(player.toDTO())
                    .build()
            );
        } else {
            Tournament waitingTournament = repository.getGameByStatusIsWaiting();
            if (waitingTournament == null) {
                waitingTournament = repository.createGame();
            }

            if (player == null) {
                player = new Player(id, request.getUsername());
                repository.addPlayer(player);
            }

            if (player.getCurrentTournament() == null) {
                player.setCurrentTournament(waitingTournament);
                waitingTournament.addPlayer(player);
            }

            eventGroup.addEvent(ConnectedEventDTO.builder()
                    .connectedPlayer(player.toDTO())
                    .build()
            );
        }

        player.setSessionId(sessionId);

        Tournament tournament = player.getCurrentTournament();
        connectedEventGroup.addEvent(InitEventDTO.builder()
                .tournament(tournament.toDto())
                .build()
        );

        if(tournament.getStatus() == TournamentStatus.WAITING &&
                tournament.getPlayers().size() == GAME_PLAYERS_QUANTITY){
            StartGameEventDTO eventDTO = start(tournament);

            eventGroup.addEvent(eventDTO);
            connectedEventGroup.addEvent(eventDTO);
        }

        if (tournament.getStatus() == TournamentStatus.ACTIVE) {
            Event nextEvent = getNextEvent(tournament);
            eventGroup.addEvent(nextEvent);
            connectedEventGroup.addEvent(nextEvent);
        }

        sendDataToAllExcludeOne("update", tournament, eventGroup, player.getSessionId());
        sendData("update", connectedEventGroup, player.getSessionId());
    }

    private StartGameEventDTO start(Tournament tournament){
        tournament.setRound(0);

        for (Player player: tournament.getPlayers()) {
            player.setStatus(PlayerStatus.PLAYING);
            player.setCurrentBalance(DEFAULT_BALANCE);
            player.setCards(tournament.getDeckOfCards().spread(2));
        }

        tournament.setStatus(TournamentStatus.ACTIVE);

        return StartGameEventDTO.builder()
                .players(
                        tournament.getPlayers().stream()
                                .map(Player::toDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public void handleDisconnected(String sessionId){
        Player player = repository.findPlayerBySessionId(sessionId);

        if (player != null) {
            Tournament tournament = player.getCurrentTournament();
            player.setActive(false);

            DisconnectedEventDTO event = DisconnectedEventDTO.builder()
                    .disconnectedPlayer(player.toDTO())
                    .build();

            sendDataToAllExcludeOne("update", tournament, event, player.getSessionId());
        }
    }

    private Event getNextEvent(Tournament tournament) {
        if (getNotFoldedPlayersCount(tournament) == 1) {
            return handleNewChampion(tournament);
        }

        if (tournament.getRound() == 0) {
            return startNewRound(tournament);
        }

        if (allBidsMatch(tournament) && allMadeBidInRound(tournament)) {
            if (tournament.getRound() == 4) {
                return handleMatchResult(tournament);
            } else {
                return startNewRound(tournament);
            }
        } else {
            return nextBid(tournament);
        }
    }

    private boolean allBidsMatch(Tournament tournament) {
        int bid = -1;

        for (Player player : tournament.getNotFoldedPlayers()) {
            if (bid != -1 && bid != player.getCurrentBid()) {
                return false;
            }

            if (player.getStatus() != PlayerStatus.ALL_IN) {
                bid = player.getCurrentBid();
            }
        }

        return true;
    }

    private boolean allMadeBidInRound(Tournament tournament) {
        for (Player player : tournament.getPlayers()) {
            if (player.getStatus() != PlayerStatus.FOLDED && !player.isMadeBetInRound()) {
                return false;
            }
        }

        return true;
    }

    public StartNewRoundEventDTO startNewRound(Tournament tournament) {
        for (Player player : tournament.getPlayers()) {
            player.setMadeBetInRound(false);
            player.setCurrentBid(0);
        }

        tournament.setRound(tournament.getRound() + 1);

        BlindDTO smallBlindResponse = null, bigBlindResponse = null;

        if (tournament.getRound() == 1) {
            Player smallBlindPlayer = tournament.getPlayers().get(0);
            tournament.setSmallBlindPlayer(smallBlindPlayer);
            smallBlindResponse = blind(smallBlindPlayer, BlindType.SMALL, tournament.getSmallBlindValue());

            Player bigBlindPlayer = tournament.getPlayers().get(1);
            tournament.setBigBlindPlayer(bigBlindPlayer);
            bigBlindResponse = blind(bigBlindPlayer, BlindType.BIG, tournament.getBigBlindValue());

            tournament.setPrizePoll(tournament.getPrizePoll() + smallBlindResponse.getBlind() + bigBlindResponse.getBlind());
        }

        tournament.setCurrentPlayer(nextPlayer(tournament, tournament.getPlayers().get(1).getId()));

        switch (tournament.getRound()) {
            case 2:
                tournament.addTableCards(tournament.getDeckOfCards().spread(3));
                break;
            case 3:
                tournament.addTableCard(tournament.getDeckOfCards().spread());
                break;
            case 4:
                tournament.addTableCard(tournament.getDeckOfCards().spread());
                break;
            default: break;
        }

        return StartNewRoundEventDTO.builder()
                .currentPlayerId(tournament.getCurrentPlayer().getId())
                .prizePool(tournament.getPrizePoll())
                .round(tournament.getRound())
                .bigBlind(bigBlindResponse)
                .smallBlind(smallBlindResponse)
                .players(tournament.getPlayers().stream()
                        .map(Player::toDTO)
                        .collect(Collectors.toList())
                )
                .tableCards(tournament.getTableCards().stream()
                        .map(ClassicCard::toDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private NextBidEventDTO nextBid(Tournament tournament) {
        Player player = nextPlayer(tournament, tournament.getCurrentPlayer().getId());
        tournament.setCurrentPlayer(player);

        return NextBidEventDTO.builder()
                .nextPlayerId(player.getId())
                .build();
    }

    private MatchResultEventDTO handleMatchResult(Tournament tournament) {
        List<Player> players = tournament.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            List<ClassicCard> cards = new ArrayList<>(tournament.getTableCards());
            cards.addAll(player.getCards());
            PokerHandResult handResult = PokerHandEval.defaultEvaluator()
                    .test(cards.toArray(new ClassicCard[0]));
            player.setHandResult(handResult);
        }

        players.sort(Comparator.comparing(Player::getHandResult).reversed());

        Player player = players.get(0);
        tournament.setStatus(TournamentStatus.FINISHED);
        MatchResultEventDTO build = MatchResultEventDTO.builder()
                .champion(player.toDTO())
                .build();

        deleteData(tournament);

        return build;
    }

    private BlindDTO blind(Player player, BlindType type, int blind) {
        int balance = player.getCurrentBalance();

        if (balance < blind) {
            player.setCurrentBid(balance);
            player.setStatus(PlayerStatus.ALL_IN);
            player.setCurrentBalance(0);
        } else {
            player.setCurrentBid(blind);
            player.setCurrentBalance(balance - blind);
        }

        return BlindDTO.builder()
                .playerId(player.getId())
                .blind(player.getCurrentBid())
                .type(type)
                .build();
    }

    private Player nextPlayer(Tournament tournament, UUID playerId) {
        List<Player> tempPlayers = new ArrayList<>(tournament.getPlayers());
        tempPlayers.addAll(tournament.getPlayers());

        boolean afterCurrent = false;
        for (Player tempPlayer : tempPlayers) {
            if (tempPlayer.getId().equals(playerId)) {
                afterCurrent = true;
            }

            if (afterCurrent && !tempPlayer.getId().equals(playerId) &&
                    tempPlayer.getStatus() != PlayerStatus.ALL_IN &&
                    tempPlayer.getStatus() != PlayerStatus.FOLDED) {
                return tempPlayer;
            }
        }

        return null;
    }

    public void handleCall(String sessionId) {
        Player player = repository.findPlayerBySessionId(sessionId);

        EventGroupDTO eventGroup = new EventGroupDTO(eventDelayInSeconds);
        eventGroup.addEvent(call(player));

        Tournament currentTournament = player.getCurrentTournament();
        eventGroup.addEvent(getNextEvent(currentTournament));

        sendDataToAll("update", currentTournament, eventGroup);
    }

    private BidResultEventDTO call(Player player) {
        Tournament tournament = player.getCurrentTournament();
        player.setMadeBetInRound(true);
        int liveBet = getLiveBet(tournament) - player.getCurrentBid();
        int bid;

        int balance = player.getCurrentBalance();
        if (balance < liveBet) {
            player.setCurrentBid(player.getCurrentBid() + balance);
            player.setStatus(PlayerStatus.ALL_IN);
            player.setCurrentBalance(0);
            bid = balance;
        } else {
            player.setCurrentBid(player.getCurrentBid() + liveBet);
            player.setCurrentBalance(balance - liveBet);
            bid = liveBet;
        }

        tournament.setPrizePoll(tournament.getPrizePoll() + bid);

        return BidResultEventDTO.builder()
                .player(player.toDTO())
                .prizePool(tournament.getPrizePoll())
                .bid(player.getCurrentBid())
                .bitType(BidType.CALL)
                .build();
    }

    public int getLiveBet(Tournament tournament){
        return tournament.getPlayers().stream()
                .map(Player::getCurrentBid).max(Comparator.naturalOrder()).get();
    }

    private ChampionEventDTO handleNewChampion(Tournament tournament) {
        tournament.setStatus(TournamentStatus.FINISHED);
        ChampionEventDTO championEventDTO = new ChampionEventDTO(getChampion(tournament).toDTO());
        deleteData(tournament);
        return championEventDTO;
    }

    private void deleteData(Tournament tournament) {
        for (Player player : tournament.getPlayers()) {
            player.setActive(true);
            player.setStatus(PlayerStatus.WAITING);
            player.setCurrentBid(0);
            player.setCards(new ArrayList<>());
            player.setCurrentBalance(0);
            player.setCurrentTournament(null);
        }

        repository.deleteTournament(tournament);
    }

    private long getNotFoldedPlayersCount(Tournament tournament) {
        return tournament.getPlayers().stream()
                .filter(p -> p.getStatus() != PlayerStatus.FOLDED)
                .count();
    }

    private Player getChampion(Tournament tournament) {
        return tournament.getPlayers().stream()
                .filter(p -> p.getStatus() != PlayerStatus.FOLDED && p.getStatus() != PlayerStatus.ALL_IN)
                .findFirst().get();
    }

    private void sendDataToAllExcludeOne(String destination, Tournament tournament, Object data, String excludeSessionId) {
        if (tournament == null || tournament.getPlayers() == null || excludeSessionId == null) {
            return;
        }

        for (Player player : tournament.getPlayers()) {
            if (!excludeSessionId.equals(player.getSessionId())) {
                sendData(destination, data, player.getSessionId());
            }
        }
    }

    private void sendDataToAll(String destination, Tournament tournament, Object data) {
        if (tournament == null || tournament.getPlayers() == null) {
            return;
        }

        for (Player player : tournament.getPlayers()) {
            sendData(destination, data, player.getSessionId());
        }
    }

    public void handleRaise(String sessionId, int bid){
        Player player = repository.findPlayerBySessionId(sessionId);

        EventGroupDTO eventGroup = new EventGroupDTO(eventDelayInSeconds);
        eventGroup.addEvent(raise(player, bid));

        Tournament currentTournament = player.getCurrentTournament();
        eventGroup.addEvent(getNextEvent(currentTournament));

        sendDataToAll("update", currentTournament, eventGroup);
    }

    private BidResultEventDTO raise(Player player, int bid) {
        Tournament tournament = player.getCurrentTournament();
        player.setMadeBetInRound(true);
        player.setCurrentBid(player.getCurrentBid() + bid);
        player.setCurrentBalance(player.getCurrentBalance() - bid);
        tournament.setPrizePoll(tournament.getPrizePoll() + bid);

        return BidResultEventDTO.builder()
                .player(player.toDTO())
                .prizePool(tournament.getPrizePoll())
                .bid(bid)
                .bitType(BidType.RAISE)
                .build();
    }

    public void handleFold(String sessionId){
        Player player = repository.findPlayerBySessionId(sessionId);

        EventGroupDTO eventGroup = new EventGroupDTO(eventDelayInSeconds);
        eventGroup.addEvent(fold(player));

        Tournament currentTournament = player.getCurrentTournament();
        eventGroup.addEvent(getNextEvent(currentTournament));

        sendDataToAll("update", currentTournament, eventGroup);
    }

    private BidResultEventDTO fold(Player player) {
        player.setStatus(PlayerStatus.FOLDED);
        player.setMadeBetInRound(true);
        return BidResultEventDTO.builder()
                .player(player.toDTO())
                .prizePool(player.getCurrentTournament().getPrizePoll())
                .bid(0)
                .bitType(BidType.FOLD)
                .build();
    }

    public void handleCheck(String sessionId) {
        Player player = repository.findPlayerBySessionId(sessionId);

        EventGroupDTO eventGroup = new EventGroupDTO(eventDelayInSeconds);
        eventGroup.addEvent(check(player));

        Tournament currentTournament = player.getCurrentTournament();
        eventGroup.addEvent(getNextEvent(currentTournament));

        sendDataToAll("update", currentTournament, eventGroup);
    }

    private BidResultEventDTO check(Player player) {
        player.setMadeBetInRound(true);
        return BidResultEventDTO.builder()
                .player(player.toDTO())
                .prizePool(player.getCurrentTournament().getPrizePoll())
                .bid(0)
                .bitType(BidType.CHECk)
                .build();
    }

    private void sendData(String destination, Object object, String sessionId){
        this.messagingTemplate.convertAndSendToUser(sessionId, "/queue/" + destination, object, createHeaders(sessionId));
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        return headerAccessor.getMessageHeaders();
    }

    public void handleNewChatMessage(String sessionId, ChatMessage message) {
        Player player = repository.findPlayerBySessionId(sessionId);

        Tournament currentTournament = player.getCurrentTournament();
        currentTournament.addChatMessage(message);

        sendDataToAll("update", currentTournament, new ChatMessageEventDTO(message));
    }
}