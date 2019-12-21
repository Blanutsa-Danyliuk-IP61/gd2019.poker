package gd2019.poker.dto;

import gd2019.poker.model.enums.PlayerStatus;
import gd2019.poker.model.enums.PokerHandType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PlayerDTO {

    private String id;
    private String name;
    private int balance;
    private int bid;
    private PlayerStatus status;
    private List<CardDTO> cards;
    private boolean active;
    private PokerHandType handType;
}
