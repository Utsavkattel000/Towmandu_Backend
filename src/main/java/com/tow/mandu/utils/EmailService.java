package com.tow.mandu.utils;

import com.google.gson.Gson;
import com.tow.mandu.config.rabbitmq.MessageProducer;
import com.tow.mandu.config.rabbitmq.RabbitMQPojo;
import com.tow.mandu.pojo.EmailPojo;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final MessageProducer messageProducer;
    private final JavaMailSender javaMailSender;
    private static final int MAX_RETRIES = 3;
    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendGeneratedPasswordMail(EmailPojo emailPojo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(emailPojo.getTo());
            message.setSubject(emailPojo.getSubject());
            message.setText(emailPojo.getBody());
            javaMailSender.send(message);
        } catch (Exception e) {
            emailPojo.setRetryCount(emailPojo.getRetryCount() + 1);
            if (emailPojo.getRetryCount() <= MAX_RETRIES)
                messageProducer.convertAndSendGeneratedPasswordMail(emailPojo);
            else
                return;
        }
    }



}
