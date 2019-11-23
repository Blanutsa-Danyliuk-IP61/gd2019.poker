package gd2019.poker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PlayerDTO {

    private String name;
    private Integer balance;
    private Integer bid;

}
