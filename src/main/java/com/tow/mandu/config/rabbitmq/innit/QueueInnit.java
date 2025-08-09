package com.tow.mandu.config.rabbitmq.innit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueInnit {
    public static final String GENERATED_PASSWORD_QUEUE = "generated-password-queue";
    public static final String ESEWA_OTP_QUEUE = "esewa-otp-queue";

    @Bean
    public Queue generatedPasswordQueue() {
        return new Queue(GENERATED_PASSWORD_QUEUE);
    }

    @Bean
    public Queue esewaOtpQueue(){
        return new Queue(ESEWA_OTP_QUEUE);
    }
}
