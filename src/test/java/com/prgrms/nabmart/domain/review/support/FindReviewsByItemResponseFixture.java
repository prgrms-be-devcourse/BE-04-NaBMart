package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByItemResponse;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindReviewsByItemResponseFixture {

    public static FindReviewsByItemResponse findReviewsByItemResponse(

    ) {
        Item givenItem = ItemFixture.item();
        Review givenReview = new Review(UserFixture.user(), givenItem, 5, "너무 맛있어요!");

        ReflectionTestUtils.setField(givenReview, "reviewId", 1L);
        ReflectionTestUtils.setField(givenItem, "itemId", 1L);
        ReflectionTestUtils.setField(givenReview, "createdAt", LocalDateTime.now());

        List<Review> givenReviews = List.of(
            givenReview
        );

        return FindReviewsByItemResponse.of(
            givenItem.getItemId(), givenReviews
        );
    }
}
