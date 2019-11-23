package gd2019.poker.model;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Tournament {

    private static final PokerHandEval eval = PokerHandEval.defaultEvaluator();
    private static final int DEFAULT_BALANCE = 1000;
    private static final int DEFAULT_SMALL_BLIND = 25;
    private static final int DEFAULT_BIG_BLIND = 10;

    private CircularList<Player> players;
    private int buttonIndex;
    private int smallBlindValue;
    private int bigBlindValue;
    private TournamentStatus status;
    private int currentPlayerIndex;
    private int games;
    private DeckOfCards deckOfCards;
    private List<ClassicCard> tableCards;
    private int rounds;

    public Tournament(){
        this.players = new CircularList<>();
        games = 0;
        buttonIndex = -1;
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

    private void removeLosers(){
        for(Player player: players){
            if(player.getCurrentBalance() == 0){
                removeLoser(player);
            }
        }
    }

    private void removeLoser(Player player){
        players.remove(player);
        player.setStatus(PlayerStatus.lose);
        player.setCurrentTournament(null);
        buttonIndex--;
    }

    public void start(){
        for (Player player: players) {
            player.setStatus(PlayerStatus.playing);
            player.setCurrentBalance(DEFAULT_BALANCE);
        }
        status = TournamentStatus.active;
        startNewGame();
    }

    private void startNewGame(){
        this.deckOfCards = new DeckOfCards();
        this.rounds = 0;
        for(Player player: players){
            player.setCards(deckOfCards.spread(2));
        }
        tableCards = deckOfCards.spread(2);
        getBidsFromBlinds();
        startRound();
    }

    private void getBidsFromBlinds(){
        Player smallBlindPlayer = players.get(buttonIndex + 1);
        smallBlindPlayer.setCurrentBid(smallBlindValue);

        Player bigBlindPlayer = players.get(buttonIndex + 2);
        bigBlindPlayer.setCurrentBid(bigBlindValue);

        currentPlayerIndex = 2;
    }

    private void startRound(){
        tableCards.add(deckOfCards.spread());
        rounds++;
    }

    private void recalculateIndexesBeforeGame(){
        buttonIndex++;
        if(games % 3 == 0){
            smallBlindValue *= 2;
            bigBlindValue *= 2;
        }
    }

    Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    private void incrementCurrentPlayerIndex(){
        do {
            currentPlayerIndex++;
        } while (!players.get(currentPlayerIndex).getActiveInGame());
    }

    public Integer getLiveBet(){
        return players.stream().map(Player::getCurrentBid).max(Comparator.naturalOrder()).get();
    }

    public void checkStatusesAfterBid(){
        if(getActiveInGamePlayers().size() == 1){
            finishGame();
        }
        if(allBidsAreMatched()){
            if(rounds == 3){
                finishGame();
            } else {
                startRound();
            }
        } else {
            incrementCurrentPlayerIndex();
        }
    }

    private void finishGame(){
        calculateHandResults();
        sortByHandResults();
        calculatePrize();
        calculateBalance();
        removeLosers();
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
            if(player.getActiveInGame()){
                int playerPrize = possiblePrize(player);
                player.setPrize(playerPrize);
                players.forEach(p -> p.setCurrentBid(Math.max(0, p.getCurrentBid() - playerPrize)));
            } else {
                player.setPrize(0);
            }
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
        Set<Integer> activePlayersCurrentBids = new HashSet<>();
        for (Player player : getActiveInGamePlayers()) {
            Integer currentBid = player.getCurrentBid();
            Integer currentBalance = player.getCurrentBalance();
            if(currentBid != 0 && currentBalance != 0){
                activePlayersCurrentBids.add(currentBid);
            }
        }
        return activePlayersCurrentBids.size() == 1;
    }

    private List<Player> getActiveInGamePlayers(){
        return players.stream().filter(Player::getActiveInGame).collect(Collectors.toList());
    }

    private void handleGameFinish(){
        if(players.size() == 1){
            Player winner = players.get(0);
            winner.setStatus(PlayerStatus.winner);
            status = TournamentStatus.finished;
            winner.setCurrentTournament(null);
        } else {
            recalculateIndexesBeforeGame();
            startNewGame();
        }
    }

}