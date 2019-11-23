package gd2019.poker.model.dto;

import gd2019.poker.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

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