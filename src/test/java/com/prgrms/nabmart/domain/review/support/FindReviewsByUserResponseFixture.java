package com.prgrms.nabmart.domain.review.support;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByUserResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindReviewsByUserResponseFixture {

    public static FindReviewsByUserResponse findReviewsByUserResponse(

    ) {
        User givenUser = UserFixture.user();
        MainCategory givenMaincategory = new MainCategory("메인");
        Item givenItem = ItemFixture.item(givenMaincategory,
            new SubCategory(givenMaincategory, "서브"));
        Review givenReview = new Review(givenUser, givenItem, 5, "너무 맛있어요!");

        ReflectionTestUtils.setField(givenReview, "reviewId", 1L);
        ReflectionTestUtils.setField(givenReview, "createdAt", LocalDateTime.now());

        List<Review> givenReviews = List.of(
            givenReview
        );

        return FindReviewsByUserResponse.of(
            givenUser, givenReviews
        );
    }
}
