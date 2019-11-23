package gd2019.poker.service;

import gd2019.poker.model.*;
import gd2019.poker.model.dto.RequestDTO;
import gd2019.poker.model.dto.ResponseDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Mykola Danyliuk
 */
@Service
public class GameService {

    private final SimpMessagingTemplate messagingTemplate;
    private static final int GAME_PLAYERS_QUANTITY = 3;

    private Repository repository;

    public GameService(Repository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public void handleResponse(ResponseDTO response){
        UUID id = response.getUserID();
        Player player = repository.getPlayerByID(id);
        EventType event = response.getEvent();
        Integer value = Integer.valueOf(response.getParameter());
        switch (event){
            case connected: {
                handleConnected(player);
                break;
            }
            case disconnected: {
                handleDisconnected(player);
                break;
            }
            case fold: {
                handleFold(player);
                break;
            }
            case check: {
                handleCheck(player);
                break;
            }
            case raise: {
                handleRaise(player, value);
                break;
            }
            case bet: {
                handleBet(player, value);
                break;
            }
            case call: {
                handleCall(player);
                break;
            }
        }
    }

    private void handleConnected(Player player){
        Tournament waitingTournament = repository.getGameByStatusIsWaiting();
        waitingTournament.addPlayer(player);
        if(waitingTournament.getPlayers().size() == GAME_PLAYERS_QUANTITY){
            waitingTournament.start();
        } else {
            waitingTournament = repository.createGame();
        }
        sendDataToUsers(waitingTournament);
    }

    private void handleDisconnected(Player player){
        Tournament tournament = player.getCurrentTournament();
        tournament.removeDisconnectedPlayer(player);
        player.setStatus(PlayerStatus.disconnected);
        sendDataToUsers(tournament);
    }

    private void handleCheck(Player player){
        Tournament tournament = player.getCurrentTournament();
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    private void handleRaise(Player player, Integer value){
        Tournament tournament = player.getCurrentTournament();
        player.setCurrentBid(value);
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    private void handleFold(Player player){
        Tournament tournament = player.getCurrentTournament();
        player.setActiveInGame(false);
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    private void handleBet(Player player, Integer value){
        Tournament tournament = player.getCurrentTournament();
        player.setCurrentBid(value);
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    private void handleCall(Player player){
        Tournament tournament = player.getCurrentTournament();
        player.setCurrentBid(tournament.getLiveBet());
        tournament.checkStatusesAfterBid();
        sendDataToUsers(tournament);
    }

    private void sendDataToUsers(Tournament tournament){
        List<Player> players = tournament.getPlayers();
        players.forEach(p -> {
            RequestDTO dto = p.toRequestDTO();
            this.messagingTemplate.convertAndSendToUser(dto.getPlayer().getName(), "/game", dto);
        });
    }

}