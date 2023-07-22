package com.sachet.orderservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.orderservice.model.Menu;
import com.sachet.orderservice.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuEventConsumer implements AcknowledgingMessageListener<String, String> {

    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;

    public MenuEventConsumer(MenuRepository menuRepository, ObjectMapper objectMapper) {
        this.menuRepository = menuRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(
            topics = {"menu-created-event"},
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(
            ConsumerRecord<String, String> consumerRecord,
            Acknowledgment acknowledgment) {
        log.info("ConsumerRecord: {}", consumerRecord);
        Menu menu = null;
        try {
            menu = objectMapper.readValue(consumerRecord.value(), Menu.class);
            log.info("VALUE RECEIVED: {}", menu);
            menuRepository.save(menu);
            assert acknowledgment != null;
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
}
