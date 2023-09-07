package com.prgrms.nabmart.domain.review.service.request;

public record RegisterReviewCommand(
    Long userId,
    Long itemId,
    double rate,
    String content
) {

    public static RegisterReviewCommand of(
        final Long userId,
        final Long itemId,
        final double rate,
        final String content
    ) {
        return new RegisterReviewCommand(userId, itemId, rate, content);
    }
}
