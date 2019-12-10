package gd2019.poker.service;

import gd2019.poker.errors.ApiError;
import gd2019.poker.model.Tournament;
import gd2019.poker.model.TournamentStatus;
import gd2019.poker.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * @author Mykola Danyliuk
 */
@Component
public class Repository {

    private List<Player> players = new ArrayList<>();
    private List<Tournament> tournaments = new ArrayList<>();

    {
        Player player = new Player(UUID.randomUUID());
        player.setName("dima");
        players.add(player);
    }

    public Player getPlayerByID(UUID id){
        for (Player player: players) {
            if(player.getId().equals(id)){
                return player;
            }
        }

        throw new ApiError("User not found");
    }

    public Tournament createGame(){
        Tournament tournament = new Tournament();
        tournament.setStatus(TournamentStatus.waiting);
        tournaments.add(tournament);
        return tournament;
    }

    public Tournament getGameByStatusIsWaiting(){
        for(Tournament tournament : tournaments){
            if(tournament.getStatus().equals(TournamentStatus.waiting)){
                return tournament;
            }
        }

        return null;
    }

    public boolean isLoginUnique(String login) {
        return players.stream().noneMatch(player -> login.equals(player.getName()));
    }

    public boolean isUserRegistered(String id) {
        return players.stream().noneMatch(player -> UUID.fromString(id).equals(player.getId()));
    }
}
