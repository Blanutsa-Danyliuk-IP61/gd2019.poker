package gd2019.poker.dto;

import gd2019.poker.model.enums.Suit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CardDTO {

    private Suit suit;
    private String text;
}
