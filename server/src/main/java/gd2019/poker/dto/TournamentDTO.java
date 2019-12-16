package gd2019.poker.dto;

import gd2019.poker.model.enums.TournamentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TournamentDTO {

    private List<PlayerDTO> players;
    private TournamentStatus status;
    private int prizePool;
}
