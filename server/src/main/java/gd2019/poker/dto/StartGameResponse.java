package gd2019.poker.dto;

import gd2019.poker.model.ClassicCard;
import gd2019.poker.model.Player;
import gd2019.poker.model.Tournament;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
public class StartGameResponse {

    private int defaultBalance;
    private List<CardDTO> cards;

    public static StartGameResponse fromPlayer(Player player) {
        return StartGameResponse.builder()
                .defaultBalance(Tournament.DEFAULT_BALANCE)
                .cards(player.getCards().stream().map(ClassicCard::toDTO).collect(Collectors.toList()))
                .build();
    }
}
