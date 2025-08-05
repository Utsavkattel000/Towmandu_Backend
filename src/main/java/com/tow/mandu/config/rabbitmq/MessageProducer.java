package com.tow.mandu.config.rabbitmq;

import com.tow.mandu.config.rabbitmq.innit.ExchangeInnit;
import com.tow.mandu.pojo.EmailPojo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void convertAndSendGeneratedPasswordMail(EmailPojo emailPojo) {
        rabbitTemplate.convertAndSend(ExchangeInnit.GENERATED_PASSWORD_EXCHANGE, MQConfig.GENERATED_PASSWORD_KEY, emailPojo);
    }
}
