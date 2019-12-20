package gd2019.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DisconnectedResponse {

    private UUID playerId;
}
