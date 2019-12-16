package gd2019.poker.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FoldResponse {

    private UUID foldedPlayerId;
    private UUID currentPlayerId;
}
