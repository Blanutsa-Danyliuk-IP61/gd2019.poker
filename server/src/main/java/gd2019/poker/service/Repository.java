package gd2019.poker.service;

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
}
