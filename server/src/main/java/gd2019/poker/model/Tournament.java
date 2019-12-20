package gd2019.poker.model;

import gd2019.poker.dto.*;
import gd2019.poker.model.enums.BlindType;
import gd2019.poker.model.enums.PlayerStatus;
import gd2019.poker.model.enums.TournamentStatus;

import java.util.*;
import java.util.stream.Collectors;

public class Tournament {

    private static final PokerHandEval eval = PokerHandEval.defaultEvaluator();
    public static final int DEFAULT_BALANCE = 1000;
    public static final int DEFAULT_SMALL_BLIND = 10;
    public static final int DEFAULT_BIG_BLIND = 20;

    private List<Player> players = new ArrayList<>();
    private int buttonIndex;
    private Player smallBlindPlayer;
    private Player bigBlindPlayer;
    private int smallBlindValue;
    private int bigBlindValue;
    private TournamentStatus status;
    private Player currentPlayer;
    private DeckOfCards deckOfCards = new DeckOfCards();
    private List<ClassicCard> tableCards = new ArrayList<>();
    private int round;
    private int prizePoll;
    private UUID smallBlindId;
    private List<ChatMessage> messages = new ArrayList<>();

    public Tournament(){
        smallBlindValue = DEFAULT_SMALL_BLIND;
        bigBlindValue = DEFAULT_BIG_BLIND;
    }

    public void addPlayer(Player player){
        players.add(player);
        player.setStatus(PlayerStatus.WAITING);
        player.setCurrentTournament(this);
    }

    public void removeDisconnectedPlayer(Player player){
        players.remove(player);
        player.setStatus(PlayerStatus.DISCONNECTED);
        player.setCurrentTournament(null);
    }

    public void start(){
        this.round = 0;

        for (Player player: players) {
            player.setStatus(PlayerStatus.PLAYING);
            player.setCurrentBalance(DEFAULT_BALANCE);
            player.setCards(deckOfCards.spread(2));
        }

        status = TournamentStatus.ACTIVE;
    }

    public StartRoundResponse startNewRound() {
        this.round++;

        if (round == 1) {
            smallBlindPlayer = players.get(0);
            bigBlindPlayer = players.get(1);
        } else {
            smallBlindPlayer = nextPlayer(smallBlindPlayer.getId());
            bigBlindPlayer = nextPlayer(bigBlindPlayer.getId());
        }
        currentPlayer = nextPlayer(bigBlindPlayer.getId());

        Player smallBlindPlayer = this.smallBlindPlayer;
        BlindResponse smallBlindResponse = blind(smallBlindPlayer, BlindType.SMALL, smallBlindValue);
        this.smallBlindId = smallBlindPlayer.getId();

        Player bigBlindPlayer = this.bigBlindPlayer;
        BlindResponse bigBlindResponse = blind(bigBlindPlayer, BlindType.BIG, bigBlindValue);

        prizePoll =+ smallBlindResponse.getBlind() + bigBlindResponse.getBlind();

        return StartRoundResponse.builder()
                .currentPlayerId(currentPlayer.getId())
                .prizePool(prizePoll)
                .bigBlind(bigBlindResponse)
                .smallBlind(smallBlindResponse)
                .roundIndex(round)
                .build();
    }

    private BlindResponse blind(Player player, BlindType type, int blind) {
        int balance = player.getCurrentBalance();

        if (balance < blind) {
            player.setCurrentBid(balance);
            player.setStatus(PlayerStatus.ALL_IN);
            player.setCurrentBalance(0);
        } else {
            player.setCurrentBid(blind);
            player.setCurrentBalance(balance - blind);
        }

        return BlindResponse.builder()
                .playerId(player.getId())
                .blind(player.getCurrentBid())
                .balance(player.getCurrentBalance())
                .type(type)
                .build();
    }

    private void startRound(){
        tableCards.add(deckOfCards.spread());
        round++;
    }

//    public Player getCurrentPlayer(){
//        return players.get(currentPlayerIndex);
//    }

    public CallResponse call(Player player) {
        int liveBet = getLiveBet();
        int bet;

        int balance = player.getCurrentBalance();
        if (balance < liveBet) {
            player.setCurrentBid(player.getCurrentBid() + balance);
            player.setStatus(PlayerStatus.ALL_IN);
            player.setCurrentBalance(0);
            bet = balance;
        } else {
            player.setCurrentBid(liveBet);
            player.setCurrentBalance(balance - liveBet);
            bet = liveBet;
        }

        prizePoll += bet;
        currentPlayer = nextPlayer(currentPlayer.getId());

        return CallResponse.builder()
                .callPlayerId(player.getId())
                .callPlayerBalance(player.getCurrentBalance())
                .prizePool(prizePoll)
                .callPlayerBet(player.getCurrentBid())
                .currentPlayerId(currentPlayer.getId())
                .callPlayerBet(bet)
                .allIn(player.getStatus() == PlayerStatus.ALL_IN)
                .build();
    }

    public FoldResponse fold(Player player) {
        player.setStatus(PlayerStatus.FOLDED);
        player.setCards(new ArrayList<>());
        player.setCurrentBid(0);

        return FoldResponse.builder()
                .foldedPlayerId(player.getId())
                .currentPlayerId(null)
                .build();
    }

    public int getLiveBet(){
        return players.stream().map(Player::getCurrentBid).max(Comparator.naturalOrder()).get();
    }

    public long getActivePlayers() {
        return players.stream().filter(p -> p.getStatus() != PlayerStatus.FOLDED && p.getStatus() != PlayerStatus.ALL_IN).count();
    }

    public Player nextPlayer(UUID playerId) {
        List<Player> tempPlayers = new ArrayList<>(players);
        tempPlayers.addAll(players);

        boolean afterCurrent = false;
        for (Player tempPlayer : tempPlayers) {
            if (tempPlayer.getId().equals(playerId)) {
                afterCurrent = true;
            }

            if (afterCurrent && !tempPlayer.getId().equals(playerId)) {
                return tempPlayer;
            }
        }

        return null;
    }

    public void checkStatusesAfterBid(){
        if (getActivePlayers() == 1){
            finishGame();
        }

//        if(allBidsAreMatched()){
//            if(round == 3){
//                finishGame();
//            } else {
//                startRound();
//            }
//        } else {
//        }
    }

    private void finishGame(){
        calculateHandResults();
        sortByHandResults();
        calculatePrize();
        calculateBalance();
        handleGameFinish();
    }

    private void calculateHandResults(){
        for (Player player : players) {
            List<ClassicCard> cards = new ArrayList<>(tableCards);
            cards.addAll(player.getCards());
            PokerHandResult handResult = eval.test(cards.toArray(new ClassicCard[0]));
            player.setHandResult(handResult);
        }
    }

    private void calculatePrize(){
        for(Player player: players){
//            if(player.getActiveInGame()){
//                int playerPrize = possiblePrize(player);
//                player.setPrize(playerPrize);
//                players.forEach(p -> p.setCurrentBid(Math.max(0, p.getCurrentBid() - playerPrize)));
//            } else {
//                player.setPrize(0);
//            }
        }
    }

    private int possiblePrize(Player player1){
        int possiblePrize = 0;
        int player1Bid = player1.getCurrentBid();
        for (Player player2: players) {
            int player2Bid = player2.getCurrentBid();
            possiblePrize += Math.min(player1Bid, player2Bid);
        }
        return possiblePrize;
    }

    private void calculateBalance(){
        players.forEach(Player::calculateBalanceAfterGame);
    }

    private void sortByHandResults(){
        players.sort(Comparator.comparing(Player::getHandResult).reversed());
    }

    private boolean allBidsAreMatched(){
        Optional<Integer> max = players.stream().map(Player::getCurrentBid).max(Integer::compareTo);

        for (Player player : players) {
            if (player.getCurrentBid() != max.get() && player.getStatus() != PlayerStatus.FOLDED &&
                    player.getStatus() != PlayerStatus.ALL_IN) {
                return false;
            }
        }

        return true;
    }

    private void handleGameFinish(){
        if(players.size() == 1){
            Player winner = players.get(0);
            status = TournamentStatus.ACTIVE;
            winner.setCurrentTournament(null);
        } else {
            //startNewGame();
        }
    }

    public TournamentDTO toDto() {
        return TournamentDTO.builder()
                .players(players.stream()
                        .map(Player::toDTO).collect(Collectors.toList())
                )
                .messages(messages)
                .prizePool(prizePoll)
                .status(status)
                .build();
    }

    public static PokerHandEval getEval() {
        return eval;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getButtonIndex() {
        return buttonIndex;
    }

    public void setButtonIndex(int buttonIndex) {
        this.buttonIndex = buttonIndex;
    }

    public int getSmallBlindValue() {
        return smallBlindValue;
    }

    public void setSmallBlindValue(int smallBlindValue) {
        this.smallBlindValue = smallBlindValue;
    }

    public int getBigBlindValue() {
        return bigBlindValue;
    }

    public void setBigBlindValue(int bigBlindValue) {
        this.bigBlindValue = bigBlindValue;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public DeckOfCards getDeckOfCards() {
        return deckOfCards;
    }

    public void setDeckOfCards(DeckOfCards deckOfCards) {
        this.deckOfCards = deckOfCards;
    }

    public List<ClassicCard> getTableCards() {
        return tableCards;
    }

    public void setTableCards(List<ClassicCard> tableCards) {
        this.tableCards = tableCards;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int rounds) {
        this.round = rounds;
    }

    public void addChatMessage(ChatMessage message) {
        this.messages.add(message);
    }

    public void removeChatMessages() {
        this.messages = new ArrayList<>();
    }
}