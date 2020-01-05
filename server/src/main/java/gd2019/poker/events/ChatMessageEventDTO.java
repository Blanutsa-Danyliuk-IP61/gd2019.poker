package gd2019.poker.events;

import gd2019.poker.dto.ChatMessage;
import gd2019.poker.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageEventDTO implements Event {

    private ChatMessage message;

    @Override
    public EventType getType() {
        return EventType.CHAT_MESSAGE;
    }
}
