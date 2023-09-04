package com.prgrms.nabmart.domain.payment.exception;

public class DuplicatePayException extends PaymentException {
    public DuplicatePayException(String message) {
        super(message);
    }
}
