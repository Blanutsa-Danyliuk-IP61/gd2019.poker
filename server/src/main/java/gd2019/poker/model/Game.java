package gd2019.poker.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Game {

    private static final int DEFAULT_BALANCE = 1000;
    private static final int DEFAULT_SMALL_BLIND = 25;
    private static final int DEFAULT_BIG_BLIND = 10;

    private List<Player> players;
    private List<Round> rounds;
    private int smallBlindIndex;
    private int bigBlindIndex;
    private int smallBlind;
    private int bigBlind;

    public Game(List<User> users) {
        players = new LinkedList<>();
        rounds = new LinkedList<>();
        smallBlindIndex = 0;
        bigBlindIndex = 1;
        smallBlind = DEFAULT_SMALL_BLIND;
        bigBlind = DEFAULT_BIG_BLIND;
        for(User user : users){
            Player player = new Player(user, DEFAULT_BALANCE);
            players.add(player);
        }
    }

    public void start(){
        do {
            Round round = new Round(this);
            rounds.add(round);
            recalculateBlinds();
        } while (haveNotWinner());
    }

    private void recalculateBlinds(){
        smallBlindIndex = (smallBlindIndex + 1) % players.size();
        bigBlindIndex = (bigBlindIndex + 1) % players.size();
        if(rounds.size() % 3 == 0){
            smallBlind *= 2;
            bigBlind *= 2;
        }
    }

    private boolean haveNotWinner(){
        return getPlayersActiveInGame().size() == 1;
    }

    public List<Player> getPlayersActiveInGame(){
        return players.stream().filter(Player::getActiveInGame).collect(Collectors.toList());
    }

}
