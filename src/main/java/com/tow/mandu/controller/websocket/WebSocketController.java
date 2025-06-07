package com.tow.mandu.controller.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendRiderNotification(String riderEmail, Object notification) {
        messagingTemplate.convertAndSend("/private/queue/notifications/" + riderEmail, notification);
    }

    @MessageMapping("/subscribe/notifications")
    public void handleSubscription(String riderEmail) {
        // Confirm subscription (optional, for testing or acknowledgment)
        messagingTemplate.convertAndSend("/private/queue/notifications/" + riderEmail,
                "Successfully subscribed to notifications for rider: " + riderEmail);
    }

}