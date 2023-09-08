package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.service.request.UpdateReviewCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UpdateReviewCommandFixture {

    private static final Long REVIEW_ID = 1L;
    private static final double RATE = 5;
    private static final String CONTENT = "정말 좋아요";

    public static UpdateReviewCommand updateReviewCommand() {
        return new UpdateReviewCommand(REVIEW_ID, RATE, CONTENT);
    }
}
