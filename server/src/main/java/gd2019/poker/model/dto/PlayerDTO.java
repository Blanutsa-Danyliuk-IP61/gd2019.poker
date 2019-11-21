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

    public static PlayerDTO toDTO(Player player){
        return PlayerDTO.builder()
                .name(player.getUser().getName())
                .balance(player.getCurrentBalance())
                .bid(player.getCurrentBid())
                .build();
    }

}
