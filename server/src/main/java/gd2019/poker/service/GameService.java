package gd2019.poker.service;

import gd2019.poker.dto.*;
import gd2019.poker.model.Player;
import gd2019.poker.model.enums.PlayerStatus;
import gd2019.poker.model.Tournament;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author Mykola Danyliuk
 */
@Component
public class GameService {

    private final SimpMessagingTemplate messagingTemplate;
    private static final int GAME_PLAYERS_QUANTITY = 3;

    private Repository repository;

    public GameService(Repository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public void handleConnected(ConnectRequest request, String sessionId){
        UUID id = UUID.fromString(request.getId());
        Player player = repository.getPlayerByID(id);

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

        player.setSessionId(sessionId);

        sendDataToAllExcludeOne("newPlayer", waitingTournament, player.toDTO(), player.getSessionId());
        sendData("init", waitingTournament.toDto(), player.getSessionId());

        if(waitingTournament.getPlayers().size() == GAME_PLAYERS_QUANTITY){
            handleStartGame(waitingTournament);
        }
    }

    private void handleStartGame(Tournament tournament) {
        tournament.start();

        sendDataToAll("startGame", tournament, StartGameResponse::fromPlayer);
    }

    private void handleStartNewRound(Tournament tournament) {
        StartRoundResponse startRoundResponse = tournament.startNewRound();

        sendDataToAll("startRound", tournament, startRoundResponse);
    }

    public void handleDisconnected(String sessionId){
        Player player = repository.findPlayerBySessionId(sessionId);

        if (player != null) {
            Tournament tournament = player.getCurrentTournament();
            player.setStatus(PlayerStatus.DISCONNECTED);

            DisconnectedResponse response = DisconnectedResponse.builder()
                    .playerId(player.getId())
                    .build();

            sendDataToAllExcludeOne("playerDisconnected", tournament, response, player.getSessionId());
        }
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

    private void sendDataToAll(String destination, Tournament tournament, Function<Player, Object> mapper) {
        if (tournament == null || tournament.getPlayers() == null) {
            return;
        }

        for (Player player : tournament.getPlayers()) {
            sendData(destination, mapper.apply(player), player.getSessionId());
        }
    }

    private void handleDisconnected(Player player){
        Tournament tournament = player.getCurrentTournament();
        tournament.removeDisconnectedPlayer(player);
        player.setStatus(PlayerStatus.DISCONNECTED);
        sendDataToUsers(tournament);
    }

//    private void handleCheck(String sessionId){
//        Player player = repository.findPlayerBySessionId(sessionId);
//        Tournament tournament = player.getCurrentTournament();
//        tournament.checkStatusesAfterBid();
//        sendDataToUsers(tournament);
//    }

    private void handleRaise(Player player, Integer value){
        Tournament tournament = player.getCurrentTournament();
        player.setCurrentBid(value);
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    private void handleFold(Player player){
        Tournament tournament = player.getCurrentTournament();
        //player.setActiveInGame(false);
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    private void handleBet(Player player, Integer value){
        Tournament tournament = player.getCurrentTournament();
        player.setCurrentBid(value);
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    public void handleCall(String sessionId) {
        Player player = repository.findPlayerBySessionId(sessionId);

        sendDataToAll("call", player.getCurrentTournament(), player.getCurrentTournament().call(player));
    }

    public void handleCheck(String sessionId) {
        Player player = repository.findPlayerBySessionId(sessionId);
    }

    public void handleFold(String sessionId) {
        Player player = repository.findPlayerBySessionId(sessionId);
    }

    private void sendDataToUsers(Tournament tournament){
        List<Player> players = tournament.getPlayers();

        players.forEach(p -> {
            //sendData(tournament, p.getSessionId());
        });
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

        sendDataToAll("message", currentTournament, message);
    }
}