package com.sachet.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreatedEventModel {
    private String id;
    private String status;
    private String userId;
    private String expiresAt;
    private String menuId;
    private Double menuPrice;
}


