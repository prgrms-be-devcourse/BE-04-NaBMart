package com.prgrms.nabmart.global.util;

public final class ErrorTemplate {

    private final String message;

    private ErrorTemplate(final String message) {
        this.message = message;
    }

    public static ErrorTemplate of(final String message) {
        return new ErrorTemplate(message);
    }

    public String getMessage() {
        return message;
    }
}