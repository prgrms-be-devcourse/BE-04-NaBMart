package com.prgrms.nabmart.domain.review.controller.request;

public record RegisterReviewRequest(
    Long itemId,
    double rate,
    String content
) {

    public static RegisterReviewRequest of(
        final Long itemId,
        final double rate,
        final String content
    ) {
        return new RegisterReviewRequest(itemId, rate, content);
    }
}
