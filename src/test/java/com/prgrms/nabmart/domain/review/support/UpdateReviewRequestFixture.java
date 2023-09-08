package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.controller.request.UpdateReviewRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UpdateReviewRequestFixture {

    private static final double RATE = 5;
    private static final String CONTENT = "정말 좋아요";

    public static UpdateReviewRequest updateReviewRequest() {
        return new UpdateReviewRequest(RATE, CONTENT);
    }
}
