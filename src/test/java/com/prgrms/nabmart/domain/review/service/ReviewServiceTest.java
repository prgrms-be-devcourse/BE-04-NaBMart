package com.prgrms.nabmart.domain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import com.prgrms.nabmart.domain.review.service.request.UpdateReviewCommand;
import com.prgrms.nabmart.domain.review.support.RegisterReviewCommandFixture;
import com.prgrms.nabmart.domain.review.support.ReviewFixture;
import com.prgrms.nabmart.domain.review.support.UpdateReviewCommandFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import com.prgrms.nabmart.global.fixture.ItemFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

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
        givenReview = ReviewFixture.review(givenUser, givenItem, 5, "내공냠냠");
    }

    @Nested
    @DisplayName("리뷰 등록 Service 실행 시")
    class RegisterReviewTest {

        RegisterReviewCommand registerReviewCommand = RegisterReviewCommandFixture.registerReviewRequest(
            1L, 1L, 5, "내공냠냠"
        );

        @Test
        @DisplayName("성공")
        void registerReview() {
            // given
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));
            given(itemRepository.findById(any())).willReturn(Optional.ofNullable(givenItem));
            given(reviewRepository.save(any())).willReturn(givenReview);

            // when
            reviewService.registerReview(registerReviewCommand);

            // then
            then(reviewRepository).should().save(any());
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 Service 실행 시")
    class DeleteReviewTest {

        @Test
        @DisplayName("성공")
        void deleteReview() {
            // given
            Long reviewId = 1L;

            given(reviewRepository.findById(any())).willReturn(Optional.ofNullable(givenReview));

            // when
            reviewService.deleteReview(reviewId);

            // then
            then(reviewRepository).should().delete(any());
        }
    }

    @Nested
    @DisplayName("리뷰 수정 Service 실행 시")
    class UpdateReviewTest {

        @Test
        @DisplayName("성공")
        void updateReview() {
            // given
            Long givenReviewId = 1L;
            double givenRate = 5;
            String givenContent = "내공냠냠";
            UpdateReviewCommand updateReviewCommand = UpdateReviewCommandFixture.updateReviewCommand(
                givenReviewId, givenRate, givenContent
            );

            given(reviewRepository.findById(any())).willReturn(Optional.ofNullable(givenReview));

            // when
            reviewService.updateReview(updateReviewCommand);

            // then
            assertThat(givenReview.getRate()).isEqualTo(givenRate);
            assertThat(givenReview.getContent()).isEqualTo(givenContent);
        }
    }
}
