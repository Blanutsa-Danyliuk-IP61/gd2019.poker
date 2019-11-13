package gd2019.poker.model;

import java.util.*;
import java.util.stream.Collectors;

public class Round {

    private static final PokerHandEval eval = PokerHandEval.defaultEvaluator();

    private List<Player> players;
    private List<ClassicCard> allCards;
    private List<ClassicCard> tableCards;
    private Game game;

    public Round(List<Player> players, Game game) {
        this.players = players;
        this.game = game;
        allCards = new LinkedList<>();
        for(Suite suite: Suite.values()){
            for(int rank = ClassicCard.RANK_2; rank <= ClassicCard.RANK_ACE; rank++){
                allCards.add(new ClassicCard(suite,rank));
            }
        }
        Collections.shuffle(allCards);
        for(Player player: players){
            player.setCards(spreadCards(2));
        }
        tableCards = spreadCards(5);
    }

    public void round(){
        for (Player player : players) {

        }
    }

    public void finish(){
        calculateHandResults();
        sortByHandResults();
        calculateBalance();
        for (Player player : players) {
            System.out.println(player.getUser().getName() + " " + player.getHandResult());
        }
        System.out.println("Winner " + players.get(0).getUser().getName());
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

    public void calculateBalance(){
        for (Player player : getPlayersInGame()) {

        }
    }

    private List<ClassicCard> spreadCards(int count){
        List<ClassicCard> cards = new ArrayList<>();
        for(int i = 0; i < count; i++){
            cards.add(spreadCard());
        }
        return cards;
    }

    private ClassicCard spreadCard(){
        return allCards.remove(0);
    }

    private List<Player> getPlayersInGame(){
        players.sort(Comparator.comparing(Player::getHandResult).reversed());
        return players.stream().filter(Player::isInGame).collect(Collectors.toList());
    }

}
