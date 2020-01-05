package gd2019.poker.events;

import gd2019.poker.dto.BlindDTO;
import gd2019.poker.dto.CardDTO;
import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.model.enums.EventType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class StartNewRoundEventDTO implements Event {

    private List<PlayerDTO> players;
    private int prizePool;
    private UUID currentPlayerId;
    private int round;
    private BlindDTO smallBlind;
    private BlindDTO bigBlind;
    private List<CardDTO> tableCards;

    @Override
    public EventType getType() {
        return EventType.NEW_ROUND;
    }
}
