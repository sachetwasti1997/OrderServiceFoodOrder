package com.sachet.orderservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.orderservice.error.OrderNotFound;
import com.sachet.orderservice.model.OrderCancelledEvent;
import com.sachet.orderservice.model.OrderStatus;
import com.sachet.orderservice.producer.OrderCancelledEventPublisher;
import com.sachet.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderExpiredConsumer implements AcknowledgingMessageListener<String, String> {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final OrderCancelledEventPublisher publisher;

    public OrderExpiredConsumer(OrderRepository orderRepository,
                                ObjectMapper objectMapper,
                                OrderCancelledEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.publisher = publisher;
    }

    @Override
    @KafkaListener(
            topics = "${spring.kafka.expireorder}",
            groupId = "${spring.kafka.orderexpireconsumer.group-id}",
            containerFactory = "kafkaOrderExpiredListenerContainerFactory"
    )
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("ConsumerRecord Order Expire: {}", consumerRecord);
        try {
            var order = objectMapper.readValue(consumerRecord.value(), OrderCancelledEvent.class);
            var orderSavedOp = orderRepository.findOrderById(order.getOrderId());
            if (orderSavedOp.isEmpty()) {
                throw new OrderNotFound("Cannot find the order", HttpStatus.NOT_FOUND);
            }
            var orderM = orderSavedOp.get();
            orderM.setStatus(OrderStatus.CANCELLED.name());
            orderRepository.save(orderM);
            var orderCancelled = new OrderCancelledEvent(order.getOrderId(), order.getMenuId());
            log.info("Sending Order Cancelled: {}", orderCancelled);
            publisher.sendOrderCancelEvent(orderCancelled);
            assert acknowledgment != null;
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
