package gd2019.poker.dto;

import gd2019.poker.model.enums.TournamentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class TournamentDTO {

    private List<PlayerDTO> players = new ArrayList<>();
    private TournamentStatus status;
    private List<ChatMessage> messages = new ArrayList<>();
    private int prizePool;
    private UUID currentPlayerId;
    private List<CardDTO> tableCards = new ArrayList<>();
}
