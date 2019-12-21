package gd2019.poker.controller;

import gd2019.poker.dto.ChatMessage;
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

    @MessageMapping("/call")
    public void call(@Header("simpSessionId") String sessionId) {
        this.gameService.handleCall(sessionId);
    }

    @MessageMapping("/check")
    public void check(@Header("simpSessionId") String sessionId) {
        this.gameService.handleCheck(sessionId);
    }

    @MessageMapping("/fold")
    public void fold(@Header("simpSessionId") String sessionId) {
        this.gameService.handleFold(sessionId);
    }

    @MessageMapping("/raise")
    public void raise(@Payload int bid, @Header("simpSessionId") String sessionId) {
        this.gameService.handleRaise(sessionId, bid);
    }

    @MessageMapping("/message")
    public void chatMessage(@Payload ChatMessage message, @Header("simpSessionId") String sessionId) {
        this.gameService.handleNewChatMessage(sessionId, message);
    }
}