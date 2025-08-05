package com.tow.mandu.utils;

import com.tow.mandu.config.firebase.Notification;
import com.tow.mandu.model.User;

public class NotificationUtil {
    public static Notification buildNotification(User user, String topic, String content) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTopic(topic);
        notification.setContent(content);
        return notification;
    }
}
