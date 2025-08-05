package com.tow.mandu.service;

import com.tow.mandu.config.firebase.Notification;

public interface NotificationService {
    Notification saveAndSend(Notification notification);
}
