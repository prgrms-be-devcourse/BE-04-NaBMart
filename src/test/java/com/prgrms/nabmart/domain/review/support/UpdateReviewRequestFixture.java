package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.controller.request.UpdateReviewRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UpdateReviewRequestFixture {

    public static UpdateReviewRequest updateReviewRequest(
        double rate, String content
    ) {
        return new UpdateReviewRequest(rate, content);
    }
}
