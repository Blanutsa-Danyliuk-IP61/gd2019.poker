package gd2019.poker.model;

public interface PokerHandResultProducer {

    final int HAND_SIZE = 5;

    PokerHandResult resultFor(PokerHandAnalyze analyze);
}
