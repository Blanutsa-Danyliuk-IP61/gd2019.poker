package gd2019.poker.model;

import lombok.Data;

import java.util.List;

@Data
public class Player {

    private User user;
    private int currentBalance;
    private int currentBid;
    private boolean inGame;
    private List<ClassicCard> cards;
    private PokerHandResult handResult;
    private Integer currentOrder;

    public Player(User user, int currentBalance) {
        this.user = user;
        this.currentBalance = currentBalance;
        this.inGame = true;
    }

    public void addBalance(int diff){
        currentBalance =+ diff;
    }

    public void subtractBalance(int diff){
        if(currentBalance - diff < 0){
            throw new IllegalStateException("Insufficient balance");
        }
        currentBalance -= diff;
    }

    public boolean isNonZeroBalance(){
        return currentBalance != 0;
    }

}
