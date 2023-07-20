package com.sachet.orderservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Menu(
        @Id
        String id,
        Double price,
        String name,
        String description,
        String specialTag,
        String category,
        String userId,
        String image
) {
}
