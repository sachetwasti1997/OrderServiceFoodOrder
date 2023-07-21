package com.sachet.orderservice.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order cannot be created as the item is not valid")
public class OrderNotFound extends RuntimeException{
    private final String message;
    private final HttpStatus status;
    public OrderNotFound(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
