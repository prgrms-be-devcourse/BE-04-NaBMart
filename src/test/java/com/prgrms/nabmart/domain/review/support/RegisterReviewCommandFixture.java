package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterReviewCommandFixture {

    public static RegisterReviewCommand registerReviewRequest(
        Long userId, Long itemId, double rate, String content
    ) {
        return new RegisterReviewCommand(userId, itemId, rate, content);
    }
}
