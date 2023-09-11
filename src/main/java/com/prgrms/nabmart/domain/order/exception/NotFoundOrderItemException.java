package com.prgrms.nabmart.domain.order.exception;

public class NotFoundOrderItemException extends OrderException {

    public NotFoundOrderItemException(final String message) {
        super(message);
    }
}
