package com.tow.mandu.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.tow.mandu.config.firebase.FCMToken;
import com.tow.mandu.config.firebase.Notification;
import com.tow.mandu.repository.FCMTokenRepository;
import com.tow.mandu.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseServiceImpl implements FirebaseService {
    private final FCMTokenRepository fcmTokenRepository;

    @Override
    public void sendPushNotification(Notification notification) {
        Optional<List<FCMToken>> fcmTokens = fcmTokenRepository.findByUserId(notification.getUser().getId());
        Map<String, String> firebaseMessageBody = new HashMap<>();
        firebaseMessageBody.put("title", notification.getTopic());
        firebaseMessageBody.put("body", notification.getContent());
        firebaseMessageBody.put("referenceId", String.valueOf(notification.getReferenceId()));
        firebaseMessageBody.put("isSeen", String.valueOf(notification.getIsSeen()));
        if (notification.getReferenceText() != null) {
            firebaseMessageBody.put("referenceText", notification.getReferenceText());
        }

        if (fcmTokens.isPresent()) {
            for (FCMToken fcmToken : fcmTokens.get()) {
                if (!fcmToken.getFcmToken().isEmpty()) {
                    try {
                        com.google.firebase.messaging.Notification firebaseNotification = com.google.firebase.messaging.Notification
                                .builder()
                                .setTitle(notification.getTopic())
                                .setBody(notification.getContent())
                                .build();
                        Message message = Message
                                .builder()
                                .setNotification(firebaseNotification)
                                .setToken(fcmToken.getFcmToken())
                                .putAllData(firebaseMessageBody)
                                .build();
                        String response = FirebaseMessaging.getInstance().send(message);
                        log.info("Successfully sent FCM message to : {} {}", fcmToken.getFcmToken(), response);
                    } catch (FirebaseMessagingException e) {
                        if (Objects.requireNonNull(e.getMessagingErrorCode()) == MessagingErrorCode.UNREGISTERED) {
                            log.error("FCM Token {} is unregistered or expired", fcmToken.getFcmToken());
                            fcmTokenRepository.delete(fcmToken);
                        } else {
                            log.error("Error sending FCM notification: {}", e.getMessagingErrorCode());
                        }
                    }
                }
            }
        }
        log.info(fcmTokens.map(tokens -> "FCM Tokens found: " + tokens.size()).orElseGet(() -> "No FCM Tokens found for user ID: " + notification.getUser().getId()));
    }

//    @Override
//    public void sendBulkPushNotification(Notification notification, List<Integer> userIds) {
//        populateIconURL(notification);
//        List<FCMToken> allFcmTokens = fcmTokenRepository.findByUserIds(userIds);
//        if (allFcmTokens.isEmpty()) {
//            log.info("No FCM tokens found for the provided user IDs");
//            return;
//        }
//        Map<String, String> firebaseMessageBody = new HashMap<>();
//        firebaseMessageBody.put("title", notification.getTopic());
//        firebaseMessageBody.put("body", notification.getContent());
//        firebaseMessageBody.put("referenceId", String.valueOf(notification.getReferenceId()));
//        firebaseMessageBody.put("type", notification.getNotificationType().name());
//        if(notification.getRedirect() != null) {
//            firebaseMessageBody.put("frontendRedirect", notification.getRedirect().frontendKey(notification.getReferenceId()));
//            firebaseMessageBody.put("mobileRedirect", notification.getRedirect().mobileKey(notification.getReferenceId()));
//        }
//        if(notification.getReferenceText()!=null){
//            firebaseMessageBody.put("referenceText", notification.getReferenceText());
//        }
//        if (notification.getNotificationIconUrl() != null) {
//            FileUrlPojo fileUrlPojo = notification.getNotificationIconUrl();
//            firebaseMessageBody.put("fileId", String.valueOf(fileUrlPojo.getId()));
//            firebaseMessageBody.put("fileName", fileUrlPojo.getName());
//            firebaseMessageBody.put("fileUrl", fileUrlPojo.getUrl());
//        }
//
//        com.google.firebase.messaging.Notification firebaseNotification = com.google.firebase.messaging.Notification
//                .builder()
//                .setTitle(notification.getTopic())
//                .setBody(notification.getContent())
//                .setImage(notification.getNotificationIconUrl() != null ? notification.getNotificationIconUrl().getUrl() : null)
//                .build();
//
//        int batchSize = 500;
//        List<FCMToken> validTokens = allFcmTokens.stream()
//                .filter(token -> !token.getFcmToken().isEmpty())
//                .toList();
//
//        for (int i = 0; i < validTokens.size(); i += batchSize) {
//            List<FCMToken> batchTokens = validTokens.subList(i, Math.min(i + batchSize, validTokens.size()));
//            List<String> tokenList = batchTokens.stream()
//                    .map(FCMToken::getFcmToken)
//                    .collect(Collectors.toList());
//
//            try {
//                MulticastMessage multicastMessage = MulticastMessage.builder()
//                        .setNotification(firebaseNotification)
//                        .putAllData(firebaseMessageBody)
//                        .addAllTokens(tokenList)
//                        .build();
//
//                BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(multicastMessage);
//
//                for (int j = 0; j < response.getResponses().size(); j++) {
//                    SendResponse sendResponse = response.getResponses().get(j);
//                    FCMToken fcmToken = batchTokens.get(j);
//                    if (sendResponse.isSuccessful()) {
//                        log.info("Successfully sent FCM message to: {}", fcmToken.getFcmToken());
//                    } else {
//                        if (sendResponse.getException() != null &&
//                                sendResponse.getException().getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
//                            log.error("FCM Token {} is unregistered or expired", fcmToken.getFcmToken());
//                            fcmTokenRepository.delete(fcmToken);
//                        } else {
//                            assert sendResponse.getException() != null;
//                            log.error("Error sending FCM notification to token {}: {}",
//                                    fcmToken.getFcmToken(), sendResponse.getException().getMessage());
//                        }
//                    }
//                }
//                log.info("Batch sent: {} successes, {} failures", response.getSuccessCount(), response.getFailureCount());
//            } catch (FirebaseMessagingException e) {
//                log.error("Error sending FCM multicast notification: {}", e.getMessage());
//            }
//        }
//
//        log.info("Total FCM tokens processed: {}", validTokens.size());
//    }

}
