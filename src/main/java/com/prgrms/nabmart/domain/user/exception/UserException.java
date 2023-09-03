package com.prgrms.nabmart.domain.user.exception;

public abstract class UserException extends RuntimeException {

    protected UserException(final String message) {
        super(message);
    }
}
