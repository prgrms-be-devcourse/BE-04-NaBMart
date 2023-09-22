package com.prgrms.nabmart.domain.order.exception;

public class UnauthorizedOrderException extends OrderException {

    public UnauthorizedOrderException(final String message) {
        super(message);
    }
}
