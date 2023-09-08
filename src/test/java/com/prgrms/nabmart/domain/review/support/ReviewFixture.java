package com.prgrms.nabmart.domain.review.support;


import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewFixture {

    private static final double RATE = 5;
    private static final String CONTENT = "맛있어요!";

    public static Review review(
        User user,
        Item item
    ) {
        return new Review(user, item, RATE, CONTENT);
    }
}
