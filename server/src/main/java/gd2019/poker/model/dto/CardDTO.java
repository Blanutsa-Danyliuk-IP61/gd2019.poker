package gd2019.poker.model.dto;

import gd2019.poker.model.Rank;
import gd2019.poker.model.Suite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CardDTO {

    private Suite suite;
    private Rank rank;

}
