package gd2019.poker.controller;

import gd2019.poker.dto.ConnectRequest;
import gd2019.poker.service.GameService;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    private GameService gameService;

    public WebsocketController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/connect")
    public void connect(@Payload ConnectRequest request, @Header("simpSessionId") String sessionId) {
        this.gameService.handleConnected(request, sessionId);
    }

    @MessageMapping("/startFirstRound")
    public void startFirstRound(@Header("simpSessionId") String sessionId) {
        this.gameService.handleStartFirstRoundResponse(sessionId);
    }

    @MessageMapping("/call")
    public void call(@Header("simpSessionId") String sessionId) {
        this.gameService.handleCall(sessionId);
    }

    @MessageMapping("/check")
    public void check(@Header("simpSessionId") String sessionId) {
        this.gameService.handleCall(sessionId);
    }

    @MessageMapping("/fold")
    public void fold(@Header("simpSessionId") String sessionId) {
        this.gameService.handleCall(sessionId);
    }
}