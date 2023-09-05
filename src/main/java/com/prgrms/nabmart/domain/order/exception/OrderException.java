package com.prgrms.nabmart.domain.order.exception;

public abstract class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }
}
