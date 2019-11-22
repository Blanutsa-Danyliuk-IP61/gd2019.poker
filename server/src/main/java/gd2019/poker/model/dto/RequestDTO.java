package gd2019.poker.model.dto;

import gd2019.poker.model.ClassicCard;
import gd2019.poker.model.Player;
import gd2019.poker.model.Round;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
public class RequestDTO {

    private UUID userID;
    private List<PlayerDTO> opponents;
    private PlayerDTO player;
    private String status;
    private List<CardDTO> tableCards;
    private List<CardDTO> playerCards;

}