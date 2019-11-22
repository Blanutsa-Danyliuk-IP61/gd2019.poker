package gd2019.poker.model.dto;

import gd2019.poker.model.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class PlayerDTO {

    private String name;
    private Integer balance;
    private Integer bid;

}
