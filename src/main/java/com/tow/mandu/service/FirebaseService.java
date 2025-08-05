package com.tow.mandu.service;

import com.tow.mandu.config.firebase.Notification;

public interface FirebaseService {
    void sendPushNotification(Notification notification);

//    void sendBulkPushNotification(Notification notification, List<Integer> userIds);
}
