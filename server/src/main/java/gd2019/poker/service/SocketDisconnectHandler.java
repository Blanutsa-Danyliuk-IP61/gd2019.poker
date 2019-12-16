package gd2019.poker.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
public class SocketDisconnectHandler implements ApplicationListener<SessionDisconnectEvent> {

    private final GameService gameService;

    public SocketDisconnectHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        gameService.handleDisconnected(sessionDisconnectEvent.getSessionId());
    }
}
