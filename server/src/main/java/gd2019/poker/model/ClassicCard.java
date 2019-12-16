package gd2019.poker.model;

import gd2019.poker.dto.CardDTO;
import gd2019.poker.model.enums.Rank;
import gd2019.poker.model.enums.Suit;
import lombok.Getter;

@Getter
public class ClassicCard {

    private final Suit suit;
    private final Rank rank;
    private final String text;

    public static final int RANK_2 = 2;
    public static final int RANK_3 = 3;
    public static final int RANK_4 = 4;
    public static final int RANK_5 = 5;
    public static final int RANK_6 = 6;
    public static final int RANK_7 = 7;
    public static final int RANK_8 = 8;
    public static final int RANK_9 = 9;
    public static final int RANK_10 = 10;
    public static final int RANK_JACK = 11;
    public static final int RANK_QUEEN = 12;
    public static final int RANK_KING = 13;
    public static final int RANK_ACE = 14;

    public ClassicCard(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = Rank.values() [ rank.getValue() - 2 ];
        this.text = rank.getText();
    }

    public CardDTO toDTO(){
        return CardDTO.builder()
                .text(text)
                .suit(suit)
                .build();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(suit).append(" ").append(rank).toString();
    }
}
