package com.prgrms.nabmart.domain.review.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.prgrms.nabmart.base.TestQueryDslConfig;
import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.support.ReviewFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    User givenUser;
    MainCategory givenMainCategory;
    SubCategory givenSubCategory;
    Item givenItem;
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
    @DisplayName("findAllByUserOrderByCreatedAt 실행 시")
    class findAllByUserOrderByCreatedAtTest {

        @Test
        @DisplayName("성공")
        public void success() {
            // given
            userRepository.save(givenUser);
            subCategoryRepository.save(givenSubCategory);
            mainCategoryRepository.save(givenMainCategory);
            itemRepository.save(givenItem);
            reviewRepository.save(givenReview);

            User newUser = new User("김춘배", "chunbae@gmail.com", "kakao", "kakaoId",
                UserRole.ROLE_USER, UserGrade.VIP, "주소");
            userRepository.save(newUser);
            reviewRepository.save(new Review(newUser, givenItem, 5, "내공냠냠"));

            // when
            List<Review> reviewsByUser = reviewRepository.findAllByUserOrderByCreatedAt(
                givenUser);

            // then
            assertThat(reviewsByUser).hasSize(1);
            assertThat(reviewsByUser.get(0).getUser().getUserId()).isEqualTo(givenUser.getUserId());
        }
    }

    @Nested
    @DisplayName("findAllByItemOrderByCreatedAt 실행 시")
    class findAllByItemOrderByCreatedAtTest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Item newItem = new Item("김치우동", 8000, "투다리 김치우동", 10, 0, 5, givenMainCategory,
                givenSubCategory);

            userRepository.save(givenUser);
            subCategoryRepository.save(givenSubCategory);
            mainCategoryRepository.save(givenMainCategory);
            itemRepository.save(givenItem);
            itemRepository.save(newItem);
            reviewRepository.save(givenReview);
            reviewRepository.save(new Review(givenUser, newItem, 5, "내공냠냠"));

            // when
            List<Review> reviewsByItem = reviewRepository.findAllByItemOrderByCreatedAt(
                givenItem);

            // then
            assertThat(reviewsByItem).hasSize(1);
            assertThat(reviewsByItem.get(0).getItem().getItemId()).isEqualTo(givenItem.getItemId());
        }
    }

    @Nested
    @DisplayName("findAverageRatingByItemId 실행 시")
    class FindAverageRatingByItemId {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            userRepository.save(givenUser);
            subCategoryRepository.save(givenSubCategory);
            mainCategoryRepository.save(givenMainCategory);
            itemRepository.save(givenItem);
            reviewRepository.save(givenReview);
            reviewRepository.save(new Review(givenUser, givenItem, 3, "그냥 그런듯"));

            // when
            Double foundAverageRate = reviewRepository.findAverageRatingByItemId(
                givenItem.getItemId());

            // then
            assertEquals(foundAverageRate, 4);
        }
    }
}
