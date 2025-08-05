package com.tow.mandu.config.rabbitmq.innit;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeInnit {
    public static final String GENERATED_PASSWORD_EXCHANGE = "generated-password-exchange";

    @Bean
    public TopicExchange generatedPasswordExchange() {
        return new TopicExchange(GENERATED_PASSWORD_EXCHANGE);
    }
}
