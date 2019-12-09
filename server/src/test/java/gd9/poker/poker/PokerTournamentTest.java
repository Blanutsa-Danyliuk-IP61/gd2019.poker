package gd9.poker.poker;

import gd2019.poker.controller.WebsocketController;
import gd2019.poker.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(WebsocketController.class)
public class PokerTournamentTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameService gameService;

    @Test
    public void moreCards() {
    }

}
