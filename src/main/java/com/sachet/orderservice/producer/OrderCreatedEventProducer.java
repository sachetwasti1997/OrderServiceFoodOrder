package com.sachet.orderservice.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.orderservice.model.Order;
import com.sachet.orderservice.model.OrderCreatedEventModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class OrderCreatedEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic1}")
    private String topic;

    public OrderCreatedEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<SendResult<String, String>> sendOrderCreatedEvent(OrderCreatedEventModel order)
            throws JsonProcessingException {
        log.info("Sending Message For Order Created Event: {}", order);
        var key = order.getId();
        var value = objectMapper.writeValueAsString(order);
        var producerRecord = new ProducerRecord<>(topic, key, value);
        return kafkaTemplate
                .send(producerRecord)
                .whenComplete(((result, throwable) -> {
                    if (throwable != null) {
                        handleFailure(key, value, throwable.getMessage());
                    } else {
                        handleSuccess(key, value, result);
                    }
                }));
    }

    private void handleSuccess(String key, String value, SendResult<String, String> result) {
        log.info("Successfully sent the event to kafka, with KEY: {}, VALUE: {}",
                key, result.getProducerRecord().value());
    }

    private void handleFailure(String key, String value, String message) {
        log.info("Unable to send message to Kafka, failed: {}", message);
    }
}
