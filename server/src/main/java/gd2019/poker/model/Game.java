package gd2019.poker.model;

import lombok.Data;

import java.util.ArrayList;
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
    private GameStatus status;

    public Game(){
        this.players = new LinkedList<>();
    }

    public Game(List<Player> players) {
        this.players = players;
        players.forEach(p -> p.setCurrentGame(this));
        rounds = new LinkedList<>();
        smallBlindIndex = 0;
        bigBlindIndex = 1;
        smallBlind = DEFAULT_SMALL_BLIND;
        bigBlind = DEFAULT_BIG_BLIND;
    }

    public void addPlayer(Player player){
        players.add(player);
        player.setStatus(PlayerStatus.waiting);
        player.setCurrentGame(this);
    }

    public void removePlayer(Player player){
        players.remove(player);
        player.setCurrentGame(null);
    }

    public void start(){
        for (Player player: players) {
            player.setStatus(PlayerStatus.playing);
        }
        status = GameStatus.active;
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

    public Round getCurrentRound(){
        return rounds.get(rounds.size()-1);
    }

}
