package com.sachet.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateProducerConfig {
    @Value("${spring.kafka.topic1}")
    private String topic;

    @Value("${spring.kafka.cancelorder}")
    private String cancelordertopic;

    @Bean
    public NewTopic orderCreatedEvent() {
        return TopicBuilder
                .name(topic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderCancelledTopic() {
        return TopicBuilder
                .name(cancelordertopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
