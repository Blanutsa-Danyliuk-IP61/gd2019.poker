package gd2019.poker.model;

public enum Rank {

    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE;

    public int getValue() {
        return ordinal() + 2;
    }
}
