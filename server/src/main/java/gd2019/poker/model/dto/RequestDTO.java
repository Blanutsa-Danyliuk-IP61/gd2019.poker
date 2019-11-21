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

    public static RequestDTO toDTO(Player player, Round round){
        List<Player> opponents = round.getPlayers();
            opponents.remove(player);
        return RequestDTO.builder()
                .userID(player.getUser().getId())
                .opponents(opponents.stream().map(Player::toDTO).collect(Collectors.toList()))
                .player(player.toDTO())
                .status(round.getGame().getStatus())
                .tableCards(round.getTableCards().stream().map(ClassicCard::toDTO).collect(Collectors.toList()))
                .playerCards(player.getCards().stream().map(ClassicCard::toDTO).collect(Collectors.toList()))
                .build();
    }

}