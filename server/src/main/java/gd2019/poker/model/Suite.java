package gd2019.poker.model;

public enum Suite {

    SPADES, HEARTS, DIAMONDS, CLUBS;

    public boolean isBlack() {
        return this.ordinal() % 2 == 0;
    }

    public boolean isRed() {
        return this.ordinal() % 2 == 1;
    }

}
