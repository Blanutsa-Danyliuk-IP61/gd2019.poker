package gd2019.poker.events;

import gd2019.poker.dto.TournamentDTO;
import gd2019.poker.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitEventDTO implements Event {

    private TournamentDTO tournament;

    @Override
    public EventType getType() {
        return EventType.INIT;
    }
}
