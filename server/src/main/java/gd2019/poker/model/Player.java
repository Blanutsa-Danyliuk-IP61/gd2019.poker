package gd2019.poker.model;

import gd2019.poker.model.dto.PlayerDTO;
import gd2019.poker.model.dto.RequestDTO;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Player {

    private static final int DEFAULT_BALANCE = 100;
    private static final String DEFAULT_NAME = "Player";

    private UUID id;
    private PlayerStatus status;
    private String name;
    private Game currentGame;
    private Integer currentBalance;
    private Integer currentBid;
    private Integer prize;
    private Boolean activeInRound;
    private List<ClassicCard> cards;
    private PokerHandResult handResult;
    private Integer currentOrder;

    public Player(UUID id){
        this.id = id;
        this.name = DEFAULT_NAME;
    }

    public Player(String name) {
        this.name = name;
    }

    public void initBeforeGame(){
        currentBalance = DEFAULT_BALANCE;
    }

    public void initBeforeRound(){
        activeInRound = getActiveInGame();
    }

    public void calculateBalanceAfterRound(){
        currentBalance += prize;
    }

    public Boolean getActiveInGame(){
        return currentBalance == 0;
    }

    public void calculatePrize(){
        if(activeInRound){

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public PlayerDTO toDTO(){
        return PlayerDTO.builder()
                .name(name)
                .balance(currentBalance)
                .bid(currentBid)
                .build();
    }

    private Round getCurrentRound(){
        return currentGame.getCurrentRound();
    }

    public RequestDTO toRequestDTO(){
        List<Player> opponents = getCurrentRound().getPlayers();
            opponents.remove(this);
        return RequestDTO.builder()
                .userID(id)
                .player(toDTO())
                .status(getCurrentGame().getStatus().name())
                .tableCards(getCurrentRound().getTableCards().stream().map(ClassicCard::toDTO).collect(Collectors.toList()))
                .playerCards(getCards().stream().map(ClassicCard::toDTO).collect(Collectors.toList()))
                .opponents(opponents.stream().map(Player::toDTO).collect(Collectors.toList()))
                .build();
    }

}