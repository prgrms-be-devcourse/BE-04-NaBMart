package com.prgrms.nabmart.domain.review.service.request;

public record UpdateReviewCommand(
    Long reviewId,
    double rate,
    String content
) {

    public static UpdateReviewCommand of(
        final Long reviewId,
        final double rate,
        final String content) {
        return new UpdateReviewCommand(reviewId, rate, content);
    }
}
