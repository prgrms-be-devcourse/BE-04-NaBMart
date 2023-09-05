package com.prgrms.nabmart.domain.event.exception;

public abstract class EventItemException extends RuntimeException {

    public EventItemException(String message) {
        super(message);
    }
}
