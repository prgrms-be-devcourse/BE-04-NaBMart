package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.controller.request.RegisterReviewRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterReviewRequestFixture {

    private static final Long ITEM_ID = 1L;
    private static final double RATE = 5;
    private static final String CONTENT = "정말 좋아요";

    public static RegisterReviewRequest registerReviewRequest() {
        return new RegisterReviewRequest(ITEM_ID, RATE, CONTENT);
    }
}
