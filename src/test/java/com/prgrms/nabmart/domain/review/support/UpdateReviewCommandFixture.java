package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.service.request.UpdateReviewCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UpdateReviewCommandFixture {

    public static UpdateReviewCommand updateReviewCommand(
        Long reviewId, double rate, String content
    ) {
        return new UpdateReviewCommand(reviewId, rate, content);
    }
}
