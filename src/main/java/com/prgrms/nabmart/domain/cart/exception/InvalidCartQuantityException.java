package com.prgrms.nabmart.domain.cart.exception;

public class InvalidCartQuantityException extends CartItemException {

    public InvalidCartQuantityException(final String message) {
        super(message);
    }
}
