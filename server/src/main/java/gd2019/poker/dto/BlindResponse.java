package gd2019.poker.dto;

import gd2019.poker.model.enums.BlindType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BlindResponse {

    private UUID playerId;
    private int balance;
    private BlindType type;
    private int blind;
}
