package gd2019.poker.events;

import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.events.Event;
import gd2019.poker.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisconnectedEventDTO implements Event {

    private PlayerDTO disconnectedPlayer;

    @Override
    public EventType getType() {
        return EventType.DISCONNECTED;
    }
}
