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
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import com.prgrms.nabmart.domain.review.service.request.UpdateReviewCommand;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByItemResponse;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByUserResponse;
import com.prgrms.nabmart.domain.review.support.RegisterReviewCommandFixture;
import com.prgrms.nabmart.domain.review.support.ReviewFixture;
import com.prgrms.nabmart.domain.review.support.UpdateReviewCommandFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
        givenReview = ReviewFixture.review(givenUser, givenItem);
    }

    @Nested
    @DisplayName("리뷰 등록 Service 실행 시")
    class RegisterReviewTest {

        RegisterReviewCommand registerReviewCommand = RegisterReviewCommandFixture.registerReviewCommand();

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
            UpdateReviewCommand updateReviewCommand = UpdateReviewCommandFixture.updateReviewCommand();

            given(reviewRepository.findById(any())).willReturn(Optional.ofNullable(givenReview));

            // when
            reviewService.updateReview(updateReviewCommand);

            // then
            assertThat(givenReview.getRate()).isEqualTo(updateReviewCommand.rate());
            assertThat(givenReview.getContent()).isEqualTo(updateReviewCommand.content());
        }
    }

    @Nested
    @DisplayName("로그인 한 사용자의 리뷰 목록 조회 Service 실행 시")
    class findReviewsByUserTest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            User newUser = new User("김춘배", "chunbae@gmail.com", "kakao", "kakaoId",
                UserRole.ROLE_USER, UserGrade.VIP, "주소");

            ReflectionTestUtils.setField(givenUser, "userId", 1L);
            ReflectionTestUtils.setField(newUser, "userId", 2L);

            List<Review> givenReviews = List.of(
                givenReview,
                new Review(givenUser, givenItem, 3.65, "내공냠냠2"),
                new Review(
                    newUser,
                    givenItem, 4, "야미야미"
                )
            );

            List<Review> givenUserReviews = givenReviews.stream()
                .filter(review -> review.getUser().getUserId() == 1L)
                .toList();

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));
            given(reviewRepository.findAllByUserOrderByCreatedAt(givenUser)).willReturn(
                givenUserReviews
            );

            // when
            FindReviewsByUserResponse findReviewsByUserResponse = reviewService.findReviewsByUser(
                1L);

            // then
            assertThat(findReviewsByUserResponse.reviews()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("상품에 따른 리뷰 목록 조회 Service 실행 시")
    class findReviewsByItemTest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Item newItem = new Item("김치우동", 8000, "투다리 김치우동", 10, 0, 5, givenMainCategory,
                givenSubCategory);
            ReflectionTestUtils.setField(givenItem, "itemId", 1L);
            ReflectionTestUtils.setField(newItem, "itemId", 2L);

            List<Review> givenReviews = List.of(
                givenReview,
                new Review(givenUser, givenItem, 3.65, "내공냠냠2"),
                new Review(
                    givenUser,
                    newItem, 4, "야미야미"
                )
            );

            List<Review> givenItemReviews = givenReviews.stream()
                .filter(review -> Objects.equals(review.getItem().getItemId(),
                    givenItem.getItemId()))
                .toList();

            given(itemRepository.findById(any())).willReturn(Optional.ofNullable(givenItem));
            given(reviewRepository.findAllByItemOrderByCreatedAt(givenItem)).willReturn(
                givenItemReviews
            );

            // when
            FindReviewsByItemResponse findReviewsByItemResponse = reviewService.findReviewsByItem(
                givenItem.getItemId());

            // then
            assertThat(findReviewsByItemResponse.reviews()).hasSize(2);
        }
    }
}
