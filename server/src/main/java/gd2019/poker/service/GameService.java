package gd2019.poker.service;

import gd2019.poker.Repository;
import gd2019.poker.model.*;
import gd2019.poker.model.dto.ResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Mykola Danyliuk
 */
@Service
public class GameService {

    private static final int GAME_PLAYERS_QUANTITY = 3;

    private Repository repository;

    public GameService(Repository repository) {
        this.repository = repository;
    }

    public void handleResponse(ResponseDTO response){
        UUID id = response.getUserID();
        Player player = repository.getPlayerByID(id);
        EventType event = response.getEvent();
        switch (event){
            case connected: {
                handleConnected(player);
                break;
            }
            case disconnected: {
                handleDisconnected(player);
                break;
            }
        }
    }

    public void handleConnected(Player player){
        Game waitingGame = repository.getGameByStatusIsWaiting();
        waitingGame.addPlayer(player);
        if(waitingGame.getPlayers().size() == GAME_PLAYERS_QUANTITY){
            waitingGame.start();
        }
        waitingGame = repository.createGame();
        waitingGame.setStatus(GameStatus.waiting);

    }

    public void handleDisconnected(Player player){
        player.getCurrentGame().removePlayer(player);
        player.setStatus(PlayerStatus.disconnected);
    }

}