package gd2019.poker.model;

import lombok.Data;

import java.util.Comparator;

@Data
public class Tournament {

    private static final int DEFAULT_BALANCE = 1000;
    private static final int DEFAULT_SMALL_BLIND = 25;
    private static final int DEFAULT_BIG_BLIND = 10;

    private CircularList<Player> players;
    private int buttonIndex;
    private int smallBlindValue;
    private int bigBlindValue;
    private TournamentStatus status;
    private int currentPlayerIndex;
    private Game game;
    private int games;


    public Tournament(){
        this.players = new CircularList<>();
        games = 0;
        buttonIndex = -1;
        smallBlindValue = DEFAULT_SMALL_BLIND;
        bigBlindValue = DEFAULT_BIG_BLIND;
    }

    public void addPlayer(Player player){
        players.add(player);
        player.setStatus(PlayerStatus.waiting);
        player.setCurrentTournament(this);
    }

    public void removeDisconnectedPlayer(Player player){
        players.remove(player);
        player.setStatus(PlayerStatus.disconnected);
        player.setCurrentTournament(null);
    }

    public void removeLosers(){
        for(Player player: players){
            if(player.getCurrentBalance() == 0){
                removeLoser(player);
            }
        }
    }

    private void removeLoser(Player player){
        players.remove(player);
        player.setStatus(PlayerStatus.lose);
        player.setCurrentTournament(null);
        buttonIndex--;
    }

    public void start(){
        for (Player player: players) {
            player.setStatus(PlayerStatus.playing);
            player.setCurrentBalance(DEFAULT_BALANCE);
        }
        status = TournamentStatus.active;
        startGame();
    }

    public void startGame(){
        game = new Game(this);
        recalculateIndexesBeforeGame();
        game.getBidsFromBlinds();
        game.startRound();
    }

    private void recalculateIndexesBeforeGame(){
        buttonIndex++;
        if(games % 3 == 0){
            smallBlindValue *= 2;
            bigBlindValue *= 2;
        }
    }

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public Game getCurrentGame(){
        return game;
    }

    public void incrementCurrentPlayerIndex(){
        do {
            currentPlayerIndex++;
        } while (!players.get(currentPlayerIndex).getActiveInGame());
    }

    public Integer getLiveBet(){
        return players.stream().map(Player::getCurrentBid).max(Comparator.naturalOrder()).get();
    }

    public void checkStatusesAfterBid(){
        getCurrentGame().checkStatuses();
    }

    public void handleWin(){
        if(players.size() == 1){
            Player winner = players.get(0);
            winner.setStatus(PlayerStatus.winner);
            status = TournamentStatus.finished;
            winner.setCurrentTournament(null);
        }
    }

}
