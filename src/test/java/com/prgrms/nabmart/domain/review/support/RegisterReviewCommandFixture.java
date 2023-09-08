package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterReviewCommandFixture {

    private static final Long USER_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private static final double RATE = 5;
    private static final String CONTENT = "정말 좋아요";

    public static RegisterReviewCommand registerReviewCommand() {
        return new RegisterReviewCommand(USER_ID, ITEM_ID, RATE, CONTENT);
    }
}
