package ru.jolice.nightverb.message;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Notifications {

    private final SimpMessagingTemplate template;

    public void send(String sessionId, Object payload) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        template.convertAndSendToUser(sessionId, "/queue/messages", payload,
                headerAccessor.getMessageHeaders());
    }

}
