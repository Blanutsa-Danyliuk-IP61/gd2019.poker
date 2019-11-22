package gd9.poker.poker;

import gd2019.poker.model.Game;
import gd2019.poker.model.Player;
import gd2019.poker.service.GameService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PokerGameTest {

    @Test
    public void moreCards() {

        Player user1 = new Player("Dimon");
        Player user2 = new Player("Mykola");
        List<Player> players = Arrays.asList(user1, user2);

        Game game = new Game(players);
        game.start();

    }

}
