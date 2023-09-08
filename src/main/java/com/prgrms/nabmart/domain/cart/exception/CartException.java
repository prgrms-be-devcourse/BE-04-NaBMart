package com.prgrms.nabmart.domain.cart.exception;

public abstract class CartException extends RuntimeException {

    public CartException(final String message) {
        super(message);
    }
}
