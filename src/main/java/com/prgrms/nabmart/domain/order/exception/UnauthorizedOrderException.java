package com.prgrms.nabmart.domain.order.exception;

public class UnauthorizedOrderException extends OrderException {

    public UnauthorizedOrderException(String message) {
        super(message);
    }
}
