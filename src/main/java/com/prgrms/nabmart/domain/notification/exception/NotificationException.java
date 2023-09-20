package com.prgrms.nabmart.domain.notification.exception;

public abstract class NotificationException extends RuntimeException {

    public NotificationException(String message) {
        super(message);
    }
}
