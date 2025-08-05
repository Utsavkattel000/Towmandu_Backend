package com.tow.mandu.config.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMQPojo {
    Integer retryCount = 0;
    String jsonObject;
    String createdDate = Instant.now().toString();

    public RabbitMQPojo(String jsonObject) {
        this.jsonObject = jsonObject;
    }
}
