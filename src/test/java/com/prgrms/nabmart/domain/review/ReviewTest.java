package com.prgrms.nabmart.domain.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.review.exception.InvalidReviewException;
import com.prgrms.nabmart.domain.review.support.ReviewFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReviewTest {

    User givenUser;
    Item givenItem;
    MainCategory givenMainCategory;
    SubCategory givenSubCategory;
    Review givenReview;

    @BeforeEach
    void setUp() {
        givenUser = UserFixture.user();
        givenMainCategory = CategoryFixture.mainCategory();
        givenSubCategory = CategoryFixture.subCategory(givenMainCategory);
        givenItem = ItemFixture.item(givenMainCategory, givenSubCategory);
    }

    @Nested
    @DisplayName("리뷰 생성 시")
    class NewReviewTest {

        @Test
        @DisplayName("성공")
        void newReview() {
            // given

            // when
            givenReview = ReviewFixture.review(givenUser, givenItem);

            // then
            assertThat(givenReview.getUser()).isEqualTo(givenUser);
        }

        @Test
        @DisplayName("예외 : User 가 null")
        void throwExceptionWhenUserIsNull() {
            // given
            User nullUser = null;

            // when
            Exception exception = catchException(() -> new Review(nullUser, givenItem, 5, "test"));

            // then
            assertThat(exception).isInstanceOf(NotFoundUserException.class);
        }

        @Test
        @DisplayName("예외 : Item 이 null")
        void throwExceptionWhenItemIsNull() {
            // given
            Item nullItem = null;

            // when
            Exception exception = catchException(() -> new Review(givenUser, nullItem, 5, "test"));

            // then
            assertThat(exception).isInstanceOf(NotFoundItemException.class);
        }

        @Test
        @DisplayName("예외 : rate 가 음수")
        void throwExceptionWhenRateIsMinus() {
            // given
            double rate = -1;

            // when
            Exception exception = catchException(
                () -> new Review(givenUser, givenItem, rate, "test"));

            // then
            assertThat(exception).isInstanceOf(InvalidReviewException.class);
        }

        @Test
        @DisplayName("예외 : content 가 Blank")
        void throwExceptionWhenContentIsBlank() {
            // given
            String content = "";

            // when
            Exception exception = catchException(
                () -> new Review(givenUser, givenItem, 5, content));

            // then
            assertThat(exception).isInstanceOf(InvalidReviewException.class);
        }

        @Test
        @DisplayName("예외 : content 가 100자 초과")
        void throwExceptionWhenContentIsMoreThanMaxContent() {
            // given
            String content = "a".repeat(101);

            // when
            Exception exception = catchException(
                () -> new Review(givenUser, givenItem, 5, content));

            // then
            assertThat(exception).isInstanceOf(InvalidReviewException.class);
        }
    }
}
