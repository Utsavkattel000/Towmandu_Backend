package com.tow.mandu.config.rabbitmq.innit;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeInnit {
    public static final String GENERATED_PASSWORD_EXCHANGE = "generated-password-exchange";
    public static final String ESEWA_OTP_EXCHANGE = "esewa-otp-exchange";

    @Bean
    public TopicExchange generatedPasswordExchange() {
        return new TopicExchange(GENERATED_PASSWORD_EXCHANGE);
    }
    @Bean
    public TopicExchange esewaOtpExchange() {
        return new TopicExchange(ESEWA_OTP_EXCHANGE);}
}
