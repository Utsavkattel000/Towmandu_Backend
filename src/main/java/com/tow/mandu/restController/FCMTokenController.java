package com.tow.mandu.restController;

import com.tow.mandu.config.firebase.Notification;
import com.tow.mandu.pojo.FCMPojo;
import com.tow.mandu.repository.UserRepository;
import com.tow.mandu.service.FCMTokenService;
import com.tow.mandu.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FCMTokenController {

    private final FCMTokenService fCMTokenService;
    private final FirebaseService firebaseService;
    private final UserRepository userRepository;

    @PostMapping("/post-fcm")
    public ResponseEntity<?> saveFCMToken(@RequestBody FCMPojo fcmPojo) {
        fCMTokenService.save(fcmPojo);
        return ResponseEntity.ok().body("FCM Token saved successfully");
    }

//    @PostMapping("/send-notification")
//    public ResponseEntity<?> sendNotification(@RequestParam Long userId) {
//        Notification notification = new Notification();
//        notification.setContent("FCM Token sent successfully");
//        notification.setTopic("FCM Token Notification");
//        notification.setUser(userRepository.findById(userId).orElseThrow());
//        firebaseService.sendPushNotification(notification);
//        return ResponseEntity.ok().body("Notification sent successfully");
//    }

}
