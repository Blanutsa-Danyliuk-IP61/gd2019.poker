package gd2019.poker.dto;

import gd2019.poker.dto.CardDTO;
import gd2019.poker.dto.PlayerDTO;
import gd2019.poker.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class RequestDTO {

    private UUID userID;
    private List<PlayerDTO> opponents;
    private PlayerDTO player;
    private String status;
    private List<CardDTO> tableCards;
    private List<CardDTO> playerCards;
    @Setter
    private List<EventType> availableEvents;
    @Setter
    private Integer maximumRaise;

}