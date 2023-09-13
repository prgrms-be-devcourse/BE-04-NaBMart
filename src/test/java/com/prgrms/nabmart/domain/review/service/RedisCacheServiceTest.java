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
import com.prgrms.nabmart.global.config.RedisConfig;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Import(RedisConfig.class)
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
        properties.setProperty("REDIS_HOST", "redisHost");
        properties.setProperty("REDIS_PORT", "6379");
    }

    @Nested
    @DisplayName("총 리뷰 수를 가져오는 Service 실행 시")
    class GetTotalReviewsByItemId {

        @Test
        @DisplayName("Redis에 값이 없으면 DB에서 가져오고, 값이 있으면 Redis에서 가져온다.")
        public void shouldGetTotalReviewsByItem() {
            // given
            MainCategory mainCategory = new MainCategory("메인카테고리");
            SubCategory subCategory = new SubCategory(mainCategory, "서브카테고리");
            Item item = new Item("라면", 1000, "야미", 4, 0, 1, mainCategory, subCategory);
            User user = new User("test", "test@gmail.com", "kakao", "kakaoId", UserRole.ROLE_USER,
                UserGrade.VIP, "인천");
            Review review = new Review(user, item, 5, "야미 인정이요");

            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            itemRepository.save(item);
            userRepository.save(user);
            reviewRepository.save(review);

            Long result = 1L;
            String cacheKey = "item:" + item.getItemId();

            // when
            long startTime = System.currentTimeMillis();

            Long dbCount = redisCacheService.getTotalReviewsByItemId(item.getItemId(), cacheKey);

            long stopTime = System.currentTimeMillis();

            long elapsedTime = stopTime - startTime;
            log.info("실행 시간 : " + elapsedTime);

            // then
            assertEquals(dbCount, result);

            log.info("DB 저장 이후 캐시 조회");

            long startTime2 = System.currentTimeMillis();

            Long cachedCount = redisCacheService.getTotalReviewsByItemId(item.getItemId(),
                cacheKey);

            long stopTime2 = System.currentTimeMillis();

            long elapsedTime2 = stopTime2 - startTime2;
            log.info("실행 시간 : " + elapsedTime2);

            assertEquals(dbCount, cachedCount);

            log.info("db : " + dbCount);
            log.info("Redis : " + cachedCount);
        }
    }
}
