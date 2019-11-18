package gd2019.poker.model;

import java.util.*;

public class Round {

    private static final PokerHandEval eval = PokerHandEval.defaultEvaluator();

    private List<Player> players;
    private DeckOfCards deckOfCards;
    private List<ClassicCard> tableCards;
    private Game game;

    public Round(Game game) {
        this.players = game.getPlayersActiveInGame();
        this.game = game;
        this.deckOfCards = new DeckOfCards();
        for(Player player: players){
            player.setCards(deckOfCards.spread(2));
        }
        tableCards = deckOfCards.spread(2);
        for(int i = 0; i < 3; i++){
            round();
        }
        finish();
    }

    public void round(){
        tableCards.add(deckOfCards.spread());
        for (Player player : players) {
            int bid = game.getSmallBlind();
            player.setCurrentBalance(player.getCurrentBalance() - bid);
            player.setCurrentBid(player.getCurrentBid() + bid);
        }
    }

    private void finish(){
        calculateHandResults();
        sortByHandResults();
        calculatePrize();
        calculateBalance();
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
            if(player.getActiveInRound()){
                int playerPrize = possiblePrize(player);
                player.setPrize(playerPrize);
                players.forEach(p -> p.setCurrentBid(Math.max(0, p.getCurrentBid()-playerPrize)));
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
        players.forEach(p -> {
            p.calculateBalanceAfterRound();
        });
    }

}
