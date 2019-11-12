package gd2019.poker.model;

/**
 * Checks for a normal STRAIGHT. Returns null if no straight was found
 */
public class PokerStraight implements PokerHandResultProducer {

    /**
     * Scans an array of ints to search for sequence of 1s. Can fill in gaps by using wildcards
     * @param ranks An array of the ranks provided by {@link PokerHandAnalyze}
     * @return The highest rank (index + 1) for which the straight started
     */
    public static int findHighestIndexForStraight(int[] ranks) {
        return findHighestIndexForStraight(ranks, ranks.length - 1);
    }

    private static int findHighestIndexForStraight(int[] ranks, int startIndex) {
        for (int i = startIndex; i >= 0; i--) {
            int count = ranks[i];
            if (count == 0) {
                return findHighestIndexForStraight(ranks, startIndex - 1);
            } else if (startIndex - i + 1 >= PokerHandResultProducer.HAND_SIZE)
                return startIndex + 1;
        }
        return -1;
    }

    @Override
    public PokerHandResult resultFor(PokerHandAnalyze analyze) {
        int straight = findHighestIndexForStraight(analyze.getRanks());
        if (straight != -1)
            return new PokerHandResult(PokerHandType.STRAIGHT, straight, 0, null);
        // We don't need to provide any kickers since we have a straight of 5 cards.

        return null;
    }
}
