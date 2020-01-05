package gd2019.poker.events;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventGroupDTO {

    private List<Event> events = new ArrayList<>();

    /* delay between events in seconds */
    private int delay;

    public void addEvent(Event event) {
        events.add(event);
    }

    public EventGroupDTO(int delay) {
        this.delay = delay;
    }
}
