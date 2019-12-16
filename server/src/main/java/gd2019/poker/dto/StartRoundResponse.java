package gd2019.poker.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StartRoundResponse {

    private BlindResponse smallBlind;
    private BlindResponse bigBlind;
    private int prizePool;
    private UUID currentPlayerId;
    private int roundIndex;
}
