package gd2019.poker.model;

import gd2019.poker.model.enums.Rank;
import gd2019.poker.model.enums.Suit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mykola Danyliuk
 */
public class DeckOfCards {

    private List<ClassicCard> allCards;

    public DeckOfCards() {
        allCards = new LinkedList<>();

        for(Suit suit : Suit.values()){
            for(Rank rank : Rank.values()){
                allCards.add(new ClassicCard(suit, rank));
            }
        }

        Collections.shuffle(allCards);
    }

    public List<ClassicCard> spread(int count){
        List<ClassicCard> cards = new LinkedList<>();
        for(int i = 0; i < count; i++){
            cards.add(spread());
        }
        return cards;
    }

    public ClassicCard spread(){
        return allCards.remove(0);
    }
}
