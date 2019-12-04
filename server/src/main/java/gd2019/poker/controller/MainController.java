package gd2019.poker.controller;

import gd2019.poker.service.GameService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    private GameService gameService;

    public MainController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/connected")
    public void handleResponse(@Payload String id, @Header("simpSessionId") String sessionId) {
        this.gameService.handleConnected(id, sessionId);
    }
}