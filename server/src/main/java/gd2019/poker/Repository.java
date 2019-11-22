package gd2019.poker;

import gd2019.poker.model.Game;
import gd2019.poker.model.GameStatus;
import gd2019.poker.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Mykola Danyliuk
 */
@Component
public class Repository {

    private List<Player> players = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    public Player getPlayerByID(UUID id){
        for (Player player: players) {
            if(player.getId().equals(id)){
                return player;
            }
        }
        Player player = new Player(id);
        players.add(player);
        return player;
    }

    public Game createGame(){
        Game game = new Game();
        games.add(game);
        return game;
    }

    public Game getGameByStatusIsWaiting(){
        for(Game game: games){
            if(game.getStatus().equals(GameStatus.waiting)){
                return game;
            }
        }
        return null;
    }

}
