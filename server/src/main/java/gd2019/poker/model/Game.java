package gd2019.poker.model;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Game {

    private static final PokerHandEval eval = PokerHandEval.defaultEvaluator();

    private List<Player> players;
    private DeckOfCards deckOfCards;
    private List<ClassicCard> tableCards;
    private Tournament tournament;
    private int rounds;

    public Game(Tournament tournament) {
        this.players = tournament.getPlayers();
        this.tournament = tournament;
        this.deckOfCards = new DeckOfCards();
        this.rounds = 0;
        for(Player player: players){
            player.setCards(deckOfCards.spread(2));
        }
        tableCards = deckOfCards.spread(2);
    }

    private void finish(){
        calculateHandResults();
        sortByHandResults();
        calculatePrize();
        calculateBalance();
        tournament.removeLosers();
        tournament.handleWin();
    }

    public void calculateHandResults(){
        for (Player player : players) {
            List<ClassicCard> cards = new ArrayList<>(tableCards);
            cards.addAll(player.getCards());
            PokerHandResult handResult = eval.test(cards.toArray(new ClassicCard[0]));
            player.setHandResult(handResult);
        }
    }

    public void sortByHandResults(){
        players.sort(Comparator.comparing(Player::getHandResult).reversed());
    }

    public void calculatePrize(){
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

    public int possiblePrize(Player player1){
        int possiblePrize = 0;
        int player1Bid = player1.getCurrentBid();
        for (Player player2: players) {
            int player2Bid = player2.getCurrentBid();
            possiblePrize += Math.min(player1Bid, player2Bid);
        }
        return possiblePrize;
    }

    public void calculateBalance(){
        players.forEach(Player::calculateBalanceAfterGame);
    }

    public void getBidsFromBlinds(){
        Player smallBlindPlayer = players.get(tournament.getButtonIndex() + 1);
        smallBlindPlayer.setCurrentBid(tournament.getSmallBlindValue());

        Player bigBlindPlayer = players.get(tournament.getButtonIndex() + 2);
        bigBlindPlayer.setCurrentBid(tournament.getBigBlindValue());

        tournament.setCurrentPlayerIndex(2);
    }

    public void startRound(){
        tableCards.add(deckOfCards.spread());
        rounds++;
    }

    public void checkStatuses(){
        if(getActiveInGamePlayers().size() == 1){
            finish();
        }
        if(allBidsAreMatched()){
            if(rounds == 3){
                finish();
            } else {
                startRound();
            }
        } else {
            tournament.incrementCurrentPlayerIndex();
        }
    }

    public List<Player> getActiveInGamePlayers(){
        return players.stream().filter(Player::getActiveInGame).collect(Collectors.toList());
    }

    public boolean allBidsAreMatched(){
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

}
