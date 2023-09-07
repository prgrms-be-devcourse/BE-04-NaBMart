package com.prgrms.nabmart.domain.delivery.exception;

public abstract class DeliveryException extends RuntimeException {

    protected DeliveryException(String message) {
        super(message);
    }
}
