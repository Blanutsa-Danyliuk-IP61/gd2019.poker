package gd2019.poker.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A helper class to analyze ranks and suits for an array of {@link ClassicCard}s. Create new using the static method {@link #analyze(ClassicCard...)}
 */
public class PokerHandAnalyze {

    private final int[] ranks = new int[ClassicCard.RANK_ACE];
    private final int[] suites = new int[Suite.values().length];
    private final ClassicCard[] cards;

    private PokerHandAnalyze(ClassicCard[] cards2) {
        this.cards = Arrays.copyOf(cards2, cards2.length);
    }
    /**
     * Create a new instance and analyze the provided cards
     * @param cards The cards to analyze
     * @return Organized analyze of the provided cards
     */
    public static PokerHandAnalyze analyze(ClassicCard... cards) {
        PokerHandAnalyze hand = new PokerHandAnalyze(cards);
        for (ClassicCard card : cards) {
            hand.ranks[card.getRank() - 1]++;
            hand.suites[card.getSuite().ordinal()]++;
        }
        return hand;
    }

    public int[] getRanks() {
        return ranks;
    }

    public ClassicCard[] getCards() {
        return cards;
    }

    public int size() {
        return cards.length;
    }
    /**
     * Create a sub-analyze which only includes wildcards and the specified suite. Useful to check for the FLUSH {@link PokerHandType}
     * @param suite The suite to filter by
     * @return A new analyze object
     */
    public PokerHandAnalyze filterBySuite(Suite suite) {
        List<ClassicCard> cards = new ArrayList<>();
        for (ClassicCard card : this.cards) {
            if (card.getSuite().equals(suite)) {
                cards.add(card);
            }
        }
        return analyze(cards.toArray(new ClassicCard[0]));
    }
}
