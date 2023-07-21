package com.sachet.orderservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order cannot be created as the item is not valid")
public class MenuNotFoundError extends RuntimeException{
    private final String message;
    private final HttpStatus httpStatus;
    public MenuNotFoundError(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.httpStatus = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
