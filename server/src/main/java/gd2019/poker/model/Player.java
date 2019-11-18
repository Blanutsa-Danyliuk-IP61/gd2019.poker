package gd2019.poker.model;

import lombok.Data;

import java.util.List;

@Data
public class Player {

    private User user;
    private Integer currentBalance;
    private Integer currentBid;
    private Integer prize;
    private Boolean activeInRound;
    private List<ClassicCard> cards;
    private PokerHandResult handResult;
    private Integer currentOrder;

    public Player(User user, int currentBalance) {
        this.user = user;
        this.currentBalance = currentBalance;
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

}
