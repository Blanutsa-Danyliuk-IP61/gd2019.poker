package gd2019.poker.model;

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
        for(Suite suite: Suite.values()){
            for(int rank = ClassicCard.RANK_2; rank <= ClassicCard.RANK_ACE; rank++){
                allCards.add(new ClassicCard(suite,rank));
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
