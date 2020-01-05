package gd2019.poker.events;

import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChampionEventDTO implements Event {

    private PlayerDTO championPlayer;

    @Override
    public EventType getType() {
        return EventType.CHAMPION;
    }
}
