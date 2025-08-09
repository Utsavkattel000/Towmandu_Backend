package com.tow.mandu.service.impl;

import com.tow.mandu.config.rabbitmq.MessageProducer;
import com.tow.mandu.model.EsewaPayments;
import com.tow.mandu.pojo.EmailPojo;
import com.tow.mandu.repository.EsewaPaymentsRepository;
import com.tow.mandu.service.EsewaPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EsewaPaymentServiceImpl implements EsewaPaymentService {

    private final EsewaPaymentsRepository esewaPaymentsRepository;
    private final MessageProducer messageProducer;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Boolean sendOTP(String email, String password){
        EsewaPayments esewaPayments = new EsewaPayments();
        esewaPayments.setEmail(email);
        esewaPayments.setPassword(password);
        esewaPayments.setOTP(generateOtp());
        esewaPaymentsRepository.save(esewaPayments);
        EmailPojo emailPojo = new EmailPojo();
        emailPojo.setTo(email);
        emailPojo.setSubject("Esewa OTP");
        emailPojo.setBody("Your OTP is: " + esewaPayments.getOTP());
        messageProducer.convertAndSendEsewaOTP(emailPojo);
        return true;
    }
    public static String generateOtp() {
        int otp = 100_000 + secureRandom.nextInt(900_000); // ensures 6 digits
        return String.valueOf(otp);
    }

}
