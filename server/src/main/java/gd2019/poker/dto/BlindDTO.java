package gd2019.poker.dto;

import gd2019.poker.model.enums.BlindType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BlindDTO {

    private UUID playerId;
    private BlindType type;
    private int blind;
}
