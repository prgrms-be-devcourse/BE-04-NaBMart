package com.prgrms.nabmart.domain.delivery.exception;

public abstract class RiderException extends RuntimeException {

    protected RiderException(final String message) {
        super(message);
    }
}
