package gd2019.poker.model.dto;

import gd2019.poker.model.EventType;
import lombok.Data;

import java.util.UUID;

@Data
public class ResponseDTO {

    private UUID userID;
    private EventType event;
    private String parameter;

}
