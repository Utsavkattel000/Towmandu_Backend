package com.tow.mandu.service.impl;

import com.tow.mandu.config.firebase.Notification;
import com.tow.mandu.repository.NotificationRepository;
import com.tow.mandu.service.FirebaseService;
import com.tow.mandu.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationrepository;
    private final FirebaseService firebaseService;

    @Override
    @Transactional
    public Notification saveAndSend(Notification notification) {
        notificationrepository.save(notification);
        firebaseService.sendPushNotification(notification);
        return notification;
    }

}
