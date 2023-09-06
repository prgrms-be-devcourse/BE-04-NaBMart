package com.prgrms.nabmart.domain.review.exception;

public abstract class ReviewException extends RuntimeException {

    public ReviewException(final String message) {
        super(message);
    }
}
