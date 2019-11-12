package gd9.poker.poker;

import gd2019.poker.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static gd2019.poker.model.ClassicCard.*;
import static gd2019.poker.model.Suite.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class PokerHandTest {

    @Test
    public void moreCards() {
        PokerHandEval eval = PokerHandEval.defaultEvaluator();

        assertPoker(PokerHandType.TWO_PAIR, 12, 10, eval.test(card(DIAMONDS, RANK_3), card(SPADES, RANK_2), card(DIAMONDS, RANK_10),
                card(CLUBS, RANK_10), card(CLUBS, RANK_7), card(SPADES, RANK_5),
                card(SPADES, RANK_QUEEN), card(HEARTS, RANK_QUEEN),
                card(HEARTS, RANK_ACE), card(DIAMONDS, RANK_7)));

        PokerHandResult eq1;
        PokerHandResult eq2;
        eq1 = eval.test(card(CLUBS, RANK_10), card(CLUBS, RANK_7), card(SPADES, RANK_KING),
                card(SPADES, RANK_QUEEN), card(HEARTS, RANK_QUEEN), card(HEARTS, RANK_ACE), card(DIAMONDS, RANK_ACE));
        eq2 = eval.test(card(CLUBS, RANK_JACK), card(CLUBS, RANK_7), card(SPADES, RANK_KING),
                card(SPADES, RANK_QUEEN), card(HEARTS, RANK_QUEEN), card(HEARTS, RANK_ACE), card(DIAMONDS, RANK_ACE));
        assertEquals(eq1, eq2);
    }
    public void assertPoker(PokerHandType type, int primary, PokerHandResult test) {
        assertPoker(type, test);
        assertEquals(primary, test.getPrimaryRank());
    }
    public void assertPoker(PokerHandType type, int primary, int secondary, PokerHandResult test) {
        assertPoker(type, primary, test);
        assertEquals(secondary, test.getSecondaryRank());
    }

    @Test
    public void royalAndFlushStraights() {
        PokerHandEval eval = PokerHandEval.defaultEvaluator();
        PokerHandResult result = eval.test(card(HEARTS, RANK_2), card(HEARTS, RANK_3), card(HEARTS, RANK_4), card(HEARTS, RANK_5), card(HEARTS, RANK_6));;

        assertPoker(PokerHandType.STRAIGHT_FLUSH, result);
    }

    @Test
    public void rankHands() {
        PokerHandEval eval = PokerHandEval.defaultEvaluator();
        PokerHandResult highCard       = eval.test(card(HEARTS, RANK_7), card(CLUBS, RANK_JACK), card(HEARTS, RANK_6), card(HEARTS, RANK_4), card(DIAMONDS, RANK_2));

        PokerHandResult pairLowKicker  = eval.test(card(HEARTS, RANK_7), card(CLUBS, RANK_7), card(HEARTS, RANK_6), card(HEARTS, RANK_4), card(HEARTS, RANK_2));
        PokerHandResult pairHighKicker = eval.test(card(HEARTS, RANK_7), card(CLUBS, RANK_7), card(HEARTS, RANK_KING), card(HEARTS, RANK_4), card(HEARTS, RANK_2));
        PokerHandResult pairHigher     = eval.test(card(HEARTS, RANK_KING), card(CLUBS, RANK_KING), card(HEARTS, RANK_6), card(HEARTS, RANK_4), card(HEARTS, RANK_2));

        PokerHandResult twoPair        = eval.test(card(HEARTS, RANK_KING), card(CLUBS, RANK_KING), card(HEARTS, RANK_6), card(DIAMONDS, RANK_6), card(HEARTS, RANK_2));
        PokerHandResult threeOfAKind   = eval.test(card(HEARTS, RANK_KING), card(CLUBS, RANK_KING), card(SPADES, RANK_KING), card(HEARTS, RANK_4), card(HEARTS, RANK_2));

        PokerHandResult flush         = eval.test(card(HEARTS, RANK_7), card(HEARTS, RANK_2), card(HEARTS, RANK_6), card(HEARTS, RANK_9), card(HEARTS, RANK_QUEEN));
        PokerHandResult fourOfAKind    = eval.test(card(HEARTS, RANK_7), card(SPADES, RANK_7), card(DIAMONDS, RANK_7), card(CLUBS, RANK_7), card(HEARTS, RANK_QUEEN));

        PokerHandResult straightFlush  = eval.test(card(HEARTS, RANK_8), card(HEARTS, RANK_9), card(HEARTS, RANK_10), card(HEARTS, RANK_JACK), card(HEARTS, RANK_QUEEN));

        // Add hands to list
        List<PokerHandResult> results = new ArrayList<>();
        assertAdd(results, PokerHandType.HIGH_CARD, highCard);
        assertAdd(results, PokerHandType.PAIR, pairLowKicker);
        assertAdd(results, PokerHandType.PAIR, pairHighKicker);
        assertAdd(results, PokerHandType.PAIR, pairHigher);
        assertAdd(results, PokerHandType.TWO_PAIR, twoPair);
        assertAdd(results, PokerHandType.THREE_OF_A_KIND, threeOfAKind);
        assertAdd(results, PokerHandType.FLUSH, flush);
        assertAdd(results, PokerHandType.FOUR_OF_A_KIND, fourOfAKind);
        assertAdd(results, PokerHandType.STRAIGHT_FLUSH, straightFlush);

        // Shuffle just for the fun of it
        Collections.shuffle(results);

        // Sort the list according to the HandResult comparable interface
        Collections.sort(results);

        // Assert the list
        Iterator<PokerHandResult> it = results.iterator();
        assertEquals(highCard, it.next());
        assertEquals(pairLowKicker, it.next());
        assertEquals(pairHighKicker, it.next());
        assertEquals(pairHigher, it.next());
        assertEquals(twoPair, it.next());
        assertEquals(threeOfAKind, it.next());
        assertEquals(flush, it.next());
        assertEquals(fourOfAKind, it.next());
        assertEquals(straightFlush, it.next());


        // Make sure that we have processed the entire list
        assertFalse("List is not completely processed", it.hasNext());
    }

    private static void assertAdd(List<PokerHandResult> results, PokerHandType type, PokerHandResult result) {
        assertPoker(type, result);
        results.add(result);
    }

    private static void assertPoker(PokerHandType type, PokerHandResult result) {
        if (type == null) {
            assertNull(result);
            return;
        }
        assertNotNull("Expected " + type, result);
        assertEquals(type, result.getType());
    }


    private static ClassicCard card(Suite suite, int rank) {
        return new ClassicCard(suite, rank);
    }

}
