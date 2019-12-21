package gd2019.poker.service;

import gd2019.poker.model.Player;
import gd2019.poker.model.Tournament;
import gd2019.poker.model.enums.TournamentStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        return null;
    }

    public Tournament createGame(){
        Tournament tournament = new Tournament();
        tournament.setStatus(TournamentStatus.WAITING);
        tournaments.add(tournament);
        return tournament;
    }

    public Tournament getGameByStatusIsWaiting(){
        for(Tournament tournament : tournaments){
            if(tournament.getStatus().equals(TournamentStatus.WAITING)){
                return tournament;
            }
        }

        return null;
    }

    public boolean isLoginUnique(String login) {
        return players.stream().noneMatch(player -> login.equals(player.getName()));
    }

    public boolean isUserRegistered(String id) {
        return players.stream().anyMatch(player -> UUID.fromString(id).equals(player.getId()));
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player findPlayerBySessionId(String sessionId) {
        for (Player player : players) {
            if (sessionId.equals(player.getSessionId())) {
                return player;
            }
        }

        return null;
    }

    public void deleteTournament(Tournament tournament) {
        this.tournaments.remove(tournament);
    }
}

