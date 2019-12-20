package gd2019.poker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StageResponse {

    private Response firstStep;
    private Response secondStep;
    private int delayInSeconds;
}
