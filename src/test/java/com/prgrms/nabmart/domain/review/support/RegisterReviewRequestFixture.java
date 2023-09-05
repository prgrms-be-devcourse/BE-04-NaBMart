package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.controller.request.RegisterReviewRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterReviewRequestFixture {

    public static RegisterReviewRequest registerReviewRequestFixture(
        Long itemId, double rate, String content
    ) {
        return new RegisterReviewRequest(itemId, rate, content);
    }
}
