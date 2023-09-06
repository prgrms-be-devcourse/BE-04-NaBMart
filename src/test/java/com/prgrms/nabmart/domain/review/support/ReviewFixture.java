package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewFixture {

    public static Review review(
        User user,
        Item item,
        double rate,
        String content
    ) {
        return new Review(user, item, rate, content);
    }
}
