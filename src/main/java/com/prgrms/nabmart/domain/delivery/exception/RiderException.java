package com.prgrms.nabmart.domain.delivery.exception;

public abstract class RiderException extends RuntimeException {

    protected RiderException(String message) {
        super(message);
    }
}
