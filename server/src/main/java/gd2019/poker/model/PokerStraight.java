package gd2019.poker.model;

import gd2019.poker.model.enums.PokerHandType;
import gd2019.poker.model.enums.Rank;

import java.util.Map;

public class PokerStraight implements PokerHandResultProducer {

    public static int findHighestIndexForStraight(Map<Rank,Integer> rankQuantities) {
        return findHighestIndexForStraight(rankQuantities, Rank.values().length - 1);
    }

    private static int findHighestIndexForStraight(Map<Rank ,Integer> rankQuantities, int startIndex) {
        for (int i = startIndex; i >= 0; i--) {
            Rank[] eRanks = Rank.values();
            Rank rank = eRanks[i];
            int count = rankQuantities.get(rank);
            if (count == 0) {
                return findHighestIndexForStraight(rankQuantities, startIndex - 1);
            } else if (startIndex - i + 1 >= PokerHandResultProducer.HAND_SIZE)
                return startIndex + 1;
        }
        return -1;
    }

    @Override
    public PokerHandResult resultFor(PokerHandAnalyze analyze) {
        int straight = findHighestIndexForStraight(analyze.getRankQuantities());
        if (straight != -1)
            return new PokerHandResult(PokerHandType.STRAIGHT, straight, 0, null);
        // We don't need to provide any kickers since we have a straight of 5 cards.

        return null;
    }
}
