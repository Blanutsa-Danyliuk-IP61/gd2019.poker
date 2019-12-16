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
    private int smallBlindValue;
    private int bigBlindValue;
    private TournamentStatus status;
    private int currentPlayerIndex;
    private DeckOfCards deckOfCards = new DeckOfCards();
    private List<ClassicCard> tableCards = new ArrayList<>();
    private int round;
    private int prizePoll;
    private UUID smallBlindId;

    public Tournament(){
        smallBlindValue = DEFAULT_SMALL_BLIND;
        bigBlindValue = DEFAULT_BIG_BLIND;
    }

    public void addPlayer(Player player){
        players.add(player);
        player.setStatus(PlayerStatus.waiting);
        player.setCurrentTournament(this);
    }

    public void removeDisconnectedPlayer(Player player){
        players.remove(player);
        player.setStatus(PlayerStatus.disconnected);
        player.setCurrentTournament(null);
    }

    public void start(){
        this.round = 0;

        for (Player player: players) {
            player.setStatus(PlayerStatus.playing);
            player.setCurrentBalance(DEFAULT_BALANCE);
            player.setCards(deckOfCards.spread(2));
        }

        status = TournamentStatus.active;
    }

    public StartRoundResponse startNewRound() {
        this.round++;

        Player smallBlindPlayer = players.get(buttonIndex);
        BlindResponse smallBlindResponse = blind(smallBlindPlayer, BlindType.SMALL, smallBlindValue);
        this.smallBlindId = smallBlindPlayer.getId();

        Player bigBlindPlayer = players.get(buttonIndex + 1);
        BlindResponse bigBlindResponse = blind(bigBlindPlayer, BlindType.BIG, bigBlindValue);

        prizePoll =+ smallBlindResponse.getBlind() + bigBlindResponse.getBlind();

        currentPlayerIndex = buttonIndex + 2;
        buttonIndex++;

        return StartRoundResponse.builder()
                .currentPlayerId(getCurrentPlayerId())
                .prizePool(prizePoll)
                .bigBlind(bigBlindResponse)
                .smallBlind(smallBlindResponse)
                .roundIndex(round)
                .build();
    }

    private UUID getCurrentPlayerId() {
        return players.get(currentPlayerIndex % players.size()).getId();
    }

    private BlindResponse blind(Player player, BlindType type, int blind) {
        int balance = player.getCurrentBalance();

        if (balance < blind) {
            player.setCurrentBid(balance);
            player.setStatus(PlayerStatus.allin);
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

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public CallResponse call(Player player) {
        int liveBet = getLiveBet();
        int bet;

        int balance = player.getCurrentBalance();
        if (balance < liveBet) {
            player.setCurrentBid(player.getCurrentBid() + balance);
            player.setStatus(PlayerStatus.allin);
            player.setCurrentBalance(0);
            bet = balance;
        } else {
            player.setCurrentBid(liveBet);
            player.setCurrentBalance(balance - liveBet);
            bet = liveBet;
        }

        prizePoll += bet;
        currentPlayerIndex++;

        return CallResponse.builder()
                .callPlayerId(player.getId())
                .callPlayerBalance(player.getCurrentBalance())
                .prizePool(prizePoll)
                .callPlayerBet(player.getCurrentBid())
                .currentPlayerId(getCurrentPlayerId())
                .callPlayerBet(bet)
                .allIn(player.getStatus() == PlayerStatus.allin)
                .build();
    }

    public FoldResponse fold(Player player) {
        player.setStatus(PlayerStatus.folded);
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

    public int getActivePlayers() {
        players.stream().filter(p -> p.getStatus() != PlayerStatus.folded && p.getStatus() != PlayerStatus.allin).count();
    }

    public Player nextPlayer() {
        List<Player> tempPlayers = new ArrayList<>(players);
        tempPlayers.addAll(players);

        Player current;
        for (Player tempPlayer : tempPlayers) {
            currentPlayerIndex
        }
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
//            currentPlayerIndex++;
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

//    private boolean allBidsAreMatched(){
//        Set<Integer> activePlayersCurrentBids = new HashSet<>();
//        for (Player player : getActiveInGamePlayers()) {
//            Integer currentBid = player.getCurrentBid();
//            Integer currentBalance = player.getCurrentBalance();
//            if(currentBid != 0 && currentBalance != 0){
//                activePlayersCurrentBids.add(currentBid);
//            }
//        }
//        return activePlayersCurrentBids.size() == 1;
//    }

    private void handleGameFinish(){
        if(players.size() == 1){
            Player winner = players.get(0);
            winner.setStatus(PlayerStatus.winner);
            status = TournamentStatus.finished;
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

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
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
}