package com.prgrms.nabmart.domain.order;

import static com.prgrms.nabmart.domain.item.support.ItemFixture.item;
import static com.prgrms.nabmart.domain.user.support.UserFixture.user;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.order.controller.OrderController;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest.CreateOrderItemRequest;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Properties;
import jdk.jfr.Name;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@Slf4j
public class OrderServiceIntegrationTest {

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
        properties.setProperty("spring.data.redis.host", "localhost");
        properties.setProperty("spring.data.redis.port", "6379");
    }

    @Autowired
    private OrderController orderController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Test
    @Name("주문 생성 동시성 테스트")
    void createOrderConcurrencyTest() throws Exception {
        // Given
        // 재고 수량이 10개인 Item
        Item givenItem = item();
        User givenUser = user();
        User givenUser2 = user();
        ReflectionTestUtils.setField(givenItem, "mainCategory", null);
        ReflectionTestUtils.setField(givenItem, "subCategory", null);

        ReflectionTestUtils.setField(givenUser2, "userId", 2L);
        Item item = itemRepository.save(givenItem);
        User user1 = userRepository.save(givenUser);
        User user2 = userRepository.save(givenUser2);
        log.info(item.toString());
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
            List.of(new CreateOrderItemRequest(item.getItemId(), 5)));

        // When
        Thread thread1 = new Thread(() -> {
            orderController.createOrder(createOrderRequest, user1.getUserId());
        });

        Thread thread2 = new Thread(() -> {
            orderController.createOrder(createOrderRequest, user2.getUserId());
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // Then
        Assertions.assertThat(itemRepository.findById(item.getItemId()).get().getQuantity())
            .isEqualTo(0);
    }
}
