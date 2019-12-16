package gd2019.poker.dto;

import gd2019.poker.model.enums.PlayerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PlayerDTO {

    private String id;
    private String name;
    private int balance;
    private int bid;
    private PlayerStatus status;
}
