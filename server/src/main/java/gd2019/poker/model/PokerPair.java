package gd2019.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Checks for PAIR, THREE_OF_A_KIND, FOUR_OF_A_KIND, and FULL_HOUSE. Returns HIGH_CARD if nothing better was found.
 */
public class PokerPair implements PokerHandResultProducer {

    @Override
    public PokerHandResult resultFor(PokerHandAnalyze analyze) {
        List<PokerHandResult> results = new ArrayList<>();
        List<PokerHandResult> pairs = new ArrayList<>();
        List<PokerHandResult> threeOfAKinds = new ArrayList<>();
        int[] ranks = analyze.getRanks();

        for (int index = ranks.length - 1; index >= 0; index--) {
            int count = ranks[index];
            if (count == 4) {
                results.add(new PokerHandResult(PokerHandType.FOUR_OF_A_KIND, index + 1, 0, analyze.getCards(), 1));
            }

            // If there already exists some four of a kinds, then there's no need to check three of a kinds or pairs.
            if (!results.isEmpty())
                continue;

            if (count == 3) {
                threeOfAKinds.add(new PokerHandResult(PokerHandType.THREE_OF_A_KIND, index + 1, 0, analyze.getCards(), 2));
            }
            else if (count == 2) {
                pairs.add(new PokerHandResult(PokerHandType.PAIR, index + 1, 0, analyze.getCards(), 3));
            }
        }

        return checkForFullHouseAndStuff(analyze, pairs, threeOfAKinds, results);
    }

    private PokerHandResult checkForFullHouseAndStuff(PokerHandAnalyze analyze, List<PokerHandResult> pairs, List<PokerHandResult> threeOfAKinds, List<PokerHandResult> results) {
        if (!results.isEmpty())
            return PokerHandResult.returnBest(results);

        PokerHandResult bestPair = PokerHandResult.returnBest(pairs);
        PokerHandResult bestThree = PokerHandResult.returnBest(threeOfAKinds);
        if (bestPair != null && bestThree != null) {
            return new PokerHandResult(PokerHandType.FULL_HOUSE, bestThree.getPrimaryRank(), bestPair.getPrimaryRank(), null, 0); // No kickers because it's a complete hand.
        }
        if (bestThree != null)
            return bestThree;

        if (pairs.size() >= 2) {
            Collections.sort(pairs);
            int a = pairs.get(pairs.size() - 1).getPrimaryRank();
            int b = pairs.get(pairs.size() - 2).getPrimaryRank();
            return new PokerHandResult(PokerHandType.TWO_PAIR, Math.max(a, b), Math.min(a, b), analyze.getCards(), 1);
        }

        if (bestPair != null)
            return bestPair;

        return new PokerHandResult(PokerHandType.HIGH_CARD, 0, 0, analyze.getCards());
    }

}