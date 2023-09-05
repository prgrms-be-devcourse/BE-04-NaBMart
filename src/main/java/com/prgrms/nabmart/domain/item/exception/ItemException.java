package com.prgrms.nabmart.domain.item.exception;

public abstract class ItemException extends RuntimeException {

    public ItemException(final String message) {
        super(message);
    }
}
