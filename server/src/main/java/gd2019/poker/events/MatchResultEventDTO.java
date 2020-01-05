package gd2019.poker.events;

import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResultEventDTO implements Event {

    private PlayerDTO champion;

    @Override
    public EventType getType() {
        return EventType.MATCH_RESULT;
    }
}
