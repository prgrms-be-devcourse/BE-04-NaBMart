package com.prgrms.nabmart.domain.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.delivery.repository.DeliveryRepository;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.delivery.service.DeliveryService;
import com.prgrms.nabmart.domain.delivery.service.request.AcceptDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.RegisterDeliveryCommand;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.order.repository.OrderItemRepository;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeliveryIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MainCategoryRepository mainCategoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    RiderRepository riderRepository;

    @Autowired
    EntityManagerFactory emf;

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
        properties.setProperty("REDIS_HOST", "localhost");
        properties.setProperty("REDIS_PORT", "6379");
        properties.setProperty("spring.data.redis.host", "localhost");
        properties.setProperty("spring.data.redis.port", "6379");
    }

    User user = UserFixture.user();
    MainCategory mainCategory = CategoryFixture.mainCategory();
    SubCategory subCategory = CategoryFixture.subCategory(mainCategory);
    Item item = ItemFixture.item(mainCategory, subCategory);
    OrderItem orderItem = new OrderItem(item, 5);
    Order order = new Order(user, List.of(orderItem));

    @BeforeEach
    void setUpData() {
        userRepository.save(user);
        mainCategoryRepository.save(mainCategory);
        subCategoryRepository.save(subCategory);
        itemRepository.save(item);
        orderRepository.save(order);
    }

    @AfterEach
    void tearDown() {
        deliveryRepository.deleteAll();
        riderRepository.deleteAll();
        orderRepository.deleteAll();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createQuery("delete from Item").executeUpdate(); // 소프트 딜리트 아이템 강제 삭제
        tx.commit();
        subCategoryRepository.deleteAll();
        mainCategoryRepository.deleteAll();
    }

    @Nested
    @DisplayName("registerDelivery 메서드 실행 시")
    class RegisterDeliveryTest {

        ExecutorService service;
        CountDownLatch latch;
        int treadPoolSize = 100;
        User employee;

        @BeforeEach
        void setUpConcurrent() {
            service = Executors.newFixedThreadPool(treadPoolSize);
            latch = new CountDownLatch(treadPoolSize);
        }

        @Test
        @DisplayName("성공: 여러명의 점원 중 한 명만 성공")
        void success() throws InterruptedException {
            //given
            Long noUseUserId = 1L;
            List<Exception> exList = new ArrayList<>();
            int estimateMinutes = 30;
            RegisterDeliveryCommand registerDeliveryCommand
                = RegisterDeliveryCommand.of(order.getOrderId(), noUseUserId, estimateMinutes);

            //when
            for(int i=0; i<treadPoolSize; i++) {
                service.execute(() -> {
                    try {
                        deliveryService.registerDelivery(registerDeliveryCommand);
                    } catch (Exception ex) {
                        exList.add(ex);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            assertThat(exList).hasSize(treadPoolSize-1);
        }
    }


    @Nested
    @DisplayName("acceptDelivery 메서드 실행 시")
    class AcceptDeliveryTest {

        ExecutorService service;
        CountDownLatch latch;

        @BeforeEach
        void setUpConcurrent() {
            service = Executors.newFixedThreadPool(4);
            latch = new CountDownLatch(4);
        }

        List<Rider> createAndSaveRiders(int end) {
            List<Rider> riders = IntStream.range(0, end)
                .mapToObj(i -> Rider.builder()
                    .username("username" + i)
                    .password("password" + i)
                    .address("address")
                    .build())
                .toList();
            riderRepository.saveAll(riders);
            return riders;
        }

        private Delivery createAndSaveDelivery() {
            Delivery delivery = new Delivery(order, 60);
            deliveryRepository.save(delivery);
            return delivery;
        }

        @Test
        @DisplayName("성공: 여러 명의 라이더 중 한 명만 성공")
        void success() throws InterruptedException {
            //given
            Delivery targetDelivery = createAndSaveDelivery();
            List<Rider> riders = createAndSaveRiders(4);

            //when
            for (int i = 0; i < 4; i++) {
                Rider rider = riders.get(i);
                service.execute(() -> {
                    AcceptDeliveryCommand command = AcceptDeliveryCommand.of(
                        targetDelivery.getDeliveryId(), rider.getRiderId());
                    try {
                        deliveryService.acceptDelivery(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Delivery findDelivery
                = deliveryRepository.findById(targetDelivery.getDeliveryId()).get();
            assertThat(findDelivery.getVersion()).isEqualTo(1);
        }
    }
}
