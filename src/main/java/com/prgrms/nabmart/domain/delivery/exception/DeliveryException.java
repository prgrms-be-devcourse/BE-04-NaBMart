package com.prgrms.nabmart.domain.delivery.exception;

public abstract class DeliveryException extends RuntimeException {

    protected DeliveryException(final String message) {
        super(message);
    }
}
