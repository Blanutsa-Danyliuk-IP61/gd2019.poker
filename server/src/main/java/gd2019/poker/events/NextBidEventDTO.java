package gd2019.poker.events;

import gd2019.poker.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NextBidEventDTO implements Event {

    private UUID nextPlayerId;

    @Override
    public EventType getType() {
        return EventType.NEXT_BID;
    }
}
