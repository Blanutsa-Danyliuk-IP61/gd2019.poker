package gd9.poker.poker;

import gd2019.poker.model.Game;
import gd2019.poker.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PokerGameTest {

    @Test
    public void moreCards() {

        User user1 = new User("Dimon");
        User user2 = new User("Mykola");
        List<User> users = Arrays.asList(user1, user2);

        Game game = new Game(users);
        game.start();

    }

}
