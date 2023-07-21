package com.sachet.orderservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Order {
    @Id
    private String id;
    private String userId;
    private String status;
    private Date expiresAt;
    private String menuId;
    private Double menuPrice;

    public Order(String id, String userId, String status, Date expiresAt, String menuId, Double menuPrice) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.expiresAt = expiresAt;
        this.menuId = menuId;
        this.menuPrice = menuPrice;
    }

    public Order() {
    }

    public String id() {
        return id;
    }

    public String userId() {
        return userId;
    }

    public String status() {
        return status;
    }

    public Date expiresAt() {
        return expiresAt;
    }

    public String menuId() {
        return menuId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Double getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(Double menuPrice) {
        this.menuPrice = menuPrice;
    }
}

