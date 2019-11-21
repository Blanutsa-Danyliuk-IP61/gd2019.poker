package gd2019.poker.model;

import gd2019.poker.model.dto.CardDTO;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ClassicCard {

    private final Suite suite;
    private final Rank rank;

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

    public ClassicCard(Suite suite, int rank) {
        this.suite = Objects.requireNonNull(suite, "Suite cannot be null");
        this.rank = Rank.values()[rank-2];
    }

    public CardDTO toDTO(){
        return CardDTO.builder()
                .suite(suite)
                .rank(rank)
                .build();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(suite).append(" ").append(rank).toString();
    }
}
