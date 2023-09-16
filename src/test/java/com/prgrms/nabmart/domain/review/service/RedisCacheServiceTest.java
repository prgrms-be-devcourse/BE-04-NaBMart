package com.prgrms.nabmart.domain.review.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.prgrms.nabmart.base.RedisTestContainerConfig;
import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
public class RedisCacheServiceTest extends RedisTestContainerConfig {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    User givenUser;
    Item givenItem;
    MainCategory givenMainCategory;
    SubCategory givenSubCategory;
    Review givenReview;

    @BeforeAll
    static void beforeAll() {
        Properties properties = System.getProperties();
        properties.setProperty("ISSUER", "issuer");
        properties.setProperty("CLIENT_SECRET", "clientSecret");
        properties.setProperty("NAVER_CLIENT_ID", "naverClientId");
        properties.setProperty("NAVER_CLIENT_SECRET", "naverClientSecret");
        properties.setProperty("KAKAO_CLIENT_ID", "kakaoClientId");
        properties.setProperty("KAKAO_CLIENT_SECRET", "kakaoClientSecret");
        properties.setProperty("REDIRECT_URI",
            "http://localhost:8080/login/oauth2/code/{registrationId}");
        properties.setProperty("EXPIRY_SECONDS", "60");
        properties.setProperty("TOSS_SUCCESS_URL", "tossSuccessUrl");
        properties.setProperty("TOSS_FAIL_URL", "tossFailUrl");
        properties.setProperty("TOSS_SECRET_KEY", "tossSecretKey");
    }

    @BeforeEach
    void setUp() {
        givenMainCategory = new MainCategory("메인카테고리");
        givenSubCategory = new SubCategory(givenMainCategory, "서브카테고리");
        givenItem = new Item("라면", 1000, "야미", 4, 0, 1, givenMainCategory, givenSubCategory);
        givenUser = new User("test", "test@gmail.com", "kakao", "kakaoId", UserRole.ROLE_USER,
            UserGrade.VIP, "인천");
        givenReview = new Review(givenUser, givenItem, 5, "야미 인정이요");
    }

    @Nested
    @DisplayName("상품의 총 리뷰 수를 가져오는 Service 실행 시")
    class GetTotalNumberOfReviewsByItem {

        @Test
        @DisplayName("Redis에 값이 없으면 DB에서 가져오고, 값이 있으면 Redis에서 가져온다.")
        public void shouldGetTotalNumberOfReviewsByItem() {
            // given
            mainCategoryRepository.save(givenMainCategory);
            subCategoryRepository.save(givenSubCategory);
            itemRepository.save(givenItem);
            userRepository.save(givenUser);
            reviewRepository.save(givenReview);

            Long result = 1L;
            String cacheKey = "reviewCount:Item:" + givenItem.getItemId();

            // when
            log.info("DB에서 총 리뷰 수 가져오기");
            long startTime = System.currentTimeMillis();

            Long dbCount = redisCacheService.getTotalNumberOfReviewsByItemId(givenItem.getItemId(),
                cacheKey);

            long stopTime = System.currentTimeMillis();

            long elapsedTime = stopTime - startTime;
            log.info("실행 시간 : " + elapsedTime);

            // then
            assertEquals(dbCount, result);

            log.info("DB 저장 이후 캐시 조회");

            long startTime2 = System.currentTimeMillis();

            Long cachedCount = redisCacheService.getTotalNumberOfReviewsByItemId(
                givenItem.getItemId(),
                cacheKey);

            long stopTime2 = System.currentTimeMillis();

            long elapsedTime2 = stopTime2 - startTime2;
            log.info("실행 시간 : " + elapsedTime2);

            assertEquals(dbCount, cachedCount);

            log.info("db : " + dbCount);
            log.info("Redis : " + cachedCount);
        }
    }

    @Nested
    @DisplayName("상품의 총 리뷰 수를 추가하는 서비스 실행 시")
    class PlusOneToTotalNumberOfReviewsByItemId {

        @Test
        @DisplayName("Redis에 값이 없으면 DB에서 가져오고, 값이 있으면 Redis에 총 리뷰 수를 +1한다.")
        void shouldPlusOneToTotalNumberOfReviewsByItemId() {
            // given
            mainCategoryRepository.save(givenMainCategory);
            subCategoryRepository.save(givenSubCategory);
            itemRepository.save(givenItem);
            userRepository.save(givenUser);
            reviewRepository.save(givenReview);

            String cacheKey = "reviewCount:Item:" + givenItem.getItemId();

            // when
            redisCacheService.plusOneToTotalNumberOfReviewsByItemId(givenItem.getItemId(),
                cacheKey);
            Long dbCount = redisCacheService.getTotalNumberOfReviewsByItemId(
                givenItem.getItemId(), cacheKey);

            Long cachedCount = redisCacheService.getTotalNumberOfReviewsByItemId(
                givenItem.getItemId(), cacheKey);

            // then
            assertEquals(dbCount, cachedCount);
        }
    }

    @Nested
    @DisplayName("상품의 총 리뷰 수를 감소하는 서비스 실행 시")
    class MinusOneToTotalNumberOfReviewsByItemId {

        @Test
        @DisplayName("Redis에 값이 없으면 DB에서 가져오고, 값이 있으면 Redis에 총 리뷰 수를 -1한다.")
        void shouldMinusOneToTotalNumberOfReviewsByItemId() {
            // given
            mainCategoryRepository.save(givenMainCategory);
            subCategoryRepository.save(givenSubCategory);
            itemRepository.save(givenItem);
            userRepository.save(givenUser);
            reviewRepository.save(givenReview);

            String cacheKey = "reviewCount:Item:" + givenItem.getItemId();

            // when
            redisCacheService.minusOneToTotalNumberOfReviewsByItemId(givenItem.getItemId(),
                cacheKey);
            Long dbCount = redisCacheService.getTotalNumberOfReviewsByItemId(
                givenItem.getItemId(), cacheKey);

            Long cachedCount = redisCacheService.getTotalNumberOfReviewsByItemId(
                givenItem.getItemId(), cacheKey);

            // then
            assertEquals(dbCount, cachedCount);
        }
    }

    @Nested
    @DisplayName("상품의 평균 평점을 가져오는 Service 실행 시")
    class GetAverageRatingOfReviewsByItem {

        @Test
        @DisplayName("Redis에 값이 없으면 DB에서 가져오고, 값이 있으면 Redis에서 가져온다.")
        void shouldGetAverageRatingOfReviewsByItem() {
            // given
            mainCategoryRepository.save(givenMainCategory);
            subCategoryRepository.save(givenSubCategory);
            itemRepository.save(givenItem);
            userRepository.save(givenUser);
            reviewRepository.save(givenReview);

            double result = 5;
            String cacheKey = "reviewCount:Item:" + givenItem.getItemId();

            // when
            log.info("DB에서 평균 평점 가져오기");
            long startTime = System.currentTimeMillis();

            double dbAverageRating = redisCacheService.getAverageRatingByItemId(
                givenItem.getItemId(), cacheKey);

            long stopTime = System.currentTimeMillis();

            long elapsedTime = stopTime - startTime;
            log.info("실행 시간 : " + elapsedTime);

            assertEquals(dbAverageRating, result);

            // then
            log.info("DB 저장 이후 캐시 조회");

            long startTime2 = System.currentTimeMillis();

            double cachedAverageRating = redisCacheService.getAverageRatingByItemId(
                givenItem.getItemId(), cacheKey);

            long stopTime2 = System.currentTimeMillis();

            long elapsedTime2 = stopTime2 - startTime2;
            log.info("실행 시간 : " + elapsedTime2);

            assertEquals(dbAverageRating, cachedAverageRating);

            log.info("db : " + dbAverageRating);
            log.info("Redis : " + cachedAverageRating);
        }
    }
}
