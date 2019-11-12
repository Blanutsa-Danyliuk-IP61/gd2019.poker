package gd2019.poker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to analyze poker hands by using a collection of {@link PokerHandResultProducer}s and return a {@link PokerHandResult}
 */
public class PokerHandEval {
    public void addTest(PokerHandResultProducer test) {
        this.tests.add(test);
    }

    private final List<PokerHandResultProducer> tests = new ArrayList<>();

    private PokerHandResult evaluate(PokerHandAnalyze analyze) {
        if (tests.isEmpty())
            throw new IllegalStateException("No PokerHandResultProducers added.");
        List<PokerHandResult> results = new ArrayList<>();

        for (PokerHandResultProducer test : tests) {
            PokerHandResult result = test.resultFor(analyze);
            if (result != null)
                results.add(result);
        }
        return PokerHandResult.returnBest(results);
    }

    /**
     * Test a bunch of cards and return the best matching 5-card {@link PokerHandResult}
     * @param cards The cards to test
     * @return The best matching 5-card Poker Hand
     */
    public PokerHandResult test(ClassicCard... cards) {
        return evaluate(PokerHandAnalyze.analyze(cards));
    }

    /**
     * Factory method to create an evaluator for the default poker hand types.
     * @return
     */
    public static PokerHandEval defaultEvaluator() {
        PokerHandEval eval = new PokerHandEval();
        eval.addTest(new PokerPair());
        eval.addTest(new PokerStraight());
        eval.addTest(new PokerFlush());
        return eval;
    }
}
