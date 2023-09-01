package com.prgrms.nabmart.domain.cart.exception;

public abstract class CartItemException extends RuntimeException {

    public CartItemException(final String message) {
        super(message);
    }
}
