package gd2019.poker.model;

import gd2019.poker.model.enums.PokerHandType;
import gd2019.poker.model.enums.Rank;
import gd2019.poker.model.enums.Suit;

import java.util.*;

/**
 * A helper class to analyze ranks and suits for an array of {@link ClassicCard}s. Create new using the static method {@link #analyze(ClassicCard...)}
 */
public class PokerHandAnalyze {

    private Map<Rank, Integer> rankQuantities;
    private Map<Suit, Integer> suiteQuantities;
    private final ClassicCard[] cards;

    private PokerHandAnalyze(ClassicCard[] cards2) {
        this.cards = Arrays.copyOf(cards2, cards2.length);

        rankQuantities = new HashMap<>();
        for (Rank rank: Rank.values()) {
            rankQuantities.put(rank, 0);
        }

        suiteQuantities = new HashMap<>();
        for(Suit suit : Suit.values()){
            suiteQuantities.put(suit, 0);
        }
    }
    /**
     * Create a new instance and analyze the provided cards
     * @param cards The cards to analyze
     * @return Organized analyze of the provided cards
     */
    public static PokerHandAnalyze analyze(ClassicCard... cards) {
        PokerHandAnalyze hand = new PokerHandAnalyze(cards);
        for (ClassicCard card : cards) {
            Suit suit = card.getSuit();
            Integer suiteQuantity = hand.suiteQuantities.get(suit);
            hand.suiteQuantities.put(suit, suiteQuantity+1);

            Rank rank = card.getRank();
            Integer rankQuantity = hand.rankQuantities.get(rank);
            hand.rankQuantities.put(rank, rankQuantity+1);
        }
        return hand;
    }

    public Map<Rank, Integer> getRankQuantities() {
        return rankQuantities;
    }

    public Map<Suit, Integer> getSuiteQuantities() {
        return suiteQuantities;
    }

    public ClassicCard[] getCards() {
        return cards;
    }

    public int size() {
        return cards.length;
    }
    /**
     * Create a sub-analyze which only includes wildcards and the specified suite. Useful to check for the FLUSH {@link PokerHandType}
     * @param suit The suite to filter by
     * @return A new analyze object
     */
    public PokerHandAnalyze filterBySuite(Suit suit) {
        List<ClassicCard> cards = new ArrayList<>();
        for (ClassicCard card : this.cards) {
            if (card.getSuit().equals(suit)) {
                cards.add(card);
            }
        }
        return analyze(cards.toArray(new ClassicCard[0]));
    }
}
