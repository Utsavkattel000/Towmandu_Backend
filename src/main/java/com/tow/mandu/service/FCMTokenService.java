package com.tow.mandu.service;

import com.tow.mandu.pojo.FCMPojo;

public interface FCMTokenService {
    Boolean save(FCMPojo fcmPojo);

    Boolean delete();
}