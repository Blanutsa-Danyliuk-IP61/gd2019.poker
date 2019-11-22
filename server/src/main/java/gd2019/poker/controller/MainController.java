package gd2019.poker.controller;

import gd2019.poker.service.GameService;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    private GameService gameService;

    public MainController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}