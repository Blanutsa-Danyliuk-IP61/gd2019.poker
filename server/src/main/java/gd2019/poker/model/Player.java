package gd2019.poker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.model.enums.PlayerStatus;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
public class Player {

    private UUID id;
    private PlayerStatus status = PlayerStatus.WAITING;
    private String name;

    @JsonIgnore
    private Tournament currentTournament;
    private int currentBalance = Tournament.DEFAULT_BALANCE;
    private int currentBid;
    private Integer prize;
    private List<ClassicCard> cards;
    private PokerHandResult handResult;
    private String sessionId;

    public Player(UUID id, String name){
        this.id = id;
        this.name = name;
    }

    public void calculateBalanceAfterGame(){
        currentBalance += prize;
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
                .id(id.toString())
                .name(name)
                .balance(currentBalance)
                .bid(currentBid)
                .status(status)
                .build();
    }
}