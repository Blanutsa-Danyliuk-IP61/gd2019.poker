package gd2019.poker.events;

import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StartGameEventDTO implements Event {

    private List<PlayerDTO> players;

    @Override
    public EventType getType() {
        return EventType.START_GAME;
    }
}
