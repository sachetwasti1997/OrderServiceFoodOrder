package com.sachet.orderservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order Already Reserved")
public class OrderAlreadyPresent extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public OrderAlreadyPresent(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
