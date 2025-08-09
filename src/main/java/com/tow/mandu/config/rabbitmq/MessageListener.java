package com.tow.mandu.config.rabbitmq;

import com.tow.mandu.config.rabbitmq.innit.QueueInnit;
import com.tow.mandu.pojo.EmailPojo;
import com.tow.mandu.utils.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageListener {
    private final EmailService emailService;

    @RabbitListener(queues = {QueueInnit.GENERATED_PASSWORD_QUEUE})
    public void generatedPasswordListener(EmailPojo emailPojo) {
        emailService.sendGeneratedPasswordMail(emailPojo);
    }

    @RabbitListener(queues = {QueueInnit.ESEWA_OTP_QUEUE})
    public void esewaOtpListener(EmailPojo emailPojo) {
        emailService.sendEsewaOtp(emailPojo);
    }

}
