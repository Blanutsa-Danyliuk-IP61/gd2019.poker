package gd2019.poker.model;

import lombok.Data;

import java.util.List;

@Data
public class RoundBank {

    private List<Player> players;
    private int sum;

}
