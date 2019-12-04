package gd2019.poker.model;

import gd2019.poker.model.dto.PlayerDTO;
import gd2019.poker.model.dto.RequestDTO;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Player {

    private static final String DEFAULT_NAME = "Player ";

    private UUID id;
    private PlayerStatus status;
    private String name;
    private Tournament currentTournament;
    private Integer currentBalance;
    private Integer currentBid;
    private Integer prize;
    private Boolean activeInGame;
    private List<ClassicCard> cards;
    private PokerHandResult handResult;
    private String sessionId;

    public Player(UUID id){
        this.id = id;
        this.name = DEFAULT_NAME + id;
    }

    public void calculateBalanceAfterGame(){
        currentBalance += prize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public PlayerDTO toDTO(){
        return PlayerDTO.builder()
                .name(name)
                .balance(currentBalance)
                .bid(currentBid)
                .build();
    }

    public RequestDTO toRequestDTO(){
        List<Player> opponents = currentTournament.getPlayers();
        opponents.remove(this);

        RequestDTO dto = RequestDTO.builder()
                .userID(id)
                .player(toDTO())
                .status(currentTournament.getStatus().name())
                .tableCards(currentTournament.getTableCards().stream().map(ClassicCard::toDTO).collect(Collectors.toList()))
                .playerCards(cards.stream().map(ClassicCard::toDTO).collect(Collectors.toList()))
                .opponents(opponents.stream().map(Player::toDTO).collect(Collectors.toList()))
                .build();

        boolean isActiveNow = currentTournament.getCurrentPlayer().equals(this);
        if(isActiveNow){
            List<EventType> eventTypes = Arrays.asList(EventType.fold, EventType.check, EventType.raise);
            dto.setAvailableEvents(eventTypes);
            dto.setMaximumRaise(currentBalance);
        }

        return dto;
    }

}