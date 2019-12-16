package gd2019.poker.model.enums;

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

    public String getText() {
        int value = getValue();

        switch(value) {
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            case 14: return "A";
            default: return value + "";
        }
    }
}
