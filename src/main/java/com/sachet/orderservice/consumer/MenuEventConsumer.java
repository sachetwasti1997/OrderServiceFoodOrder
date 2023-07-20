package com.sachet.orderservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuEventConsumer implements AcknowledgingMessageListener<String, String> {
    @Override
    @KafkaListener(topics = {"menu-created-event"})
    public void onMessage(
            ConsumerRecord<String, String> consumerRecord,
            Acknowledgment acknowledgment) {
        log.info("ConsumerRecord: {}", consumerRecord);
        assert acknowledgment != null;
        acknowledgment.acknowledge();
    }
}
