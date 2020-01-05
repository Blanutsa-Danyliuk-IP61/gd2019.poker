package gd2019.poker.events;

import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.model.enums.BidType;
import gd2019.poker.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BidResultEventDTO implements Event {

    private BidType bitType;
    private PlayerDTO player;
    private int prizePool;
    private int bid;

    @Override
    public EventType getType() {
        return EventType.BID_RESULT;
    }
}
