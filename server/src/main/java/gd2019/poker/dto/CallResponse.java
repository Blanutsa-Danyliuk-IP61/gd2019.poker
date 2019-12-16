package gd2019.poker.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CallResponse {

    private UUID callPlayerId;
    private int callPlayerBalance;
    private int callPlayerBet;
    private int prizePool;
    private UUID currentPlayerId;
    private boolean allIn;
}
