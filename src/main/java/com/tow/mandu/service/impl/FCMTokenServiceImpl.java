package com.tow.mandu.service.impl;

import com.tow.mandu.config.firebase.FCMToken;
import com.tow.mandu.pojo.FCMPojo;
import com.tow.mandu.repository.FCMTokenRepository;
import com.tow.mandu.service.FCMTokenService;
import com.tow.mandu.service.UserService;
import com.tow.mandu.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FCMTokenServiceImpl implements FCMTokenService {

    private final FCMTokenRepository fCMTokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Boolean save(FCMPojo fcmPojo) {
        FCMToken fcmToken = fCMTokenRepository.findByFcmToken(fcmPojo.getToken());
        if (Objects.nonNull(fcmToken)) {
            fcmToken.setFcmToken(fcmPojo.getToken());
            fcmToken.setUserId(jwtUtil.getUserFromToken().getId());
        } else {
            fcmToken = new FCMToken();
            fcmToken.setFcmToken(fcmPojo.getToken());
            fcmToken.setUserId(jwtUtil.getUserFromToken().getId());
        }
        fCMTokenRepository.save(fcmToken);
        return true;
    }

    @Override
    public Boolean delete() {
        fCMTokenRepository.deleteByUserId(jwtUtil.getUserFromToken().getId());
        return true;
    }

}
