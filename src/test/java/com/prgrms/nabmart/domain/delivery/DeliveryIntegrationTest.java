package com.prgrms.nabmart.domain.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.base.IntegrationTest;
import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.delivery.service.DeliveryService;
import com.prgrms.nabmart.domain.delivery.service.request.AcceptDeliveryCommand;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderItem;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeliveryIntegrationTest extends IntegrationTest {

    @Autowired
    DeliveryService deliveryService;

    @Nested
    @DisplayName("acceptDelivery 메서드 실행 시")
    class AcceptDeliveryTest {

        ExecutorService service;
        CountDownLatch latch;

        User user = UserFixture.user();
        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = CategoryFixture.subCategory(mainCategory);
        Item item = ItemFixture.item(mainCategory, subCategory);
        OrderItem orderItem = new OrderItem(item, 5);
        Order order = new Order(user, List.of(orderItem));

        @BeforeEach
        void setUpConcurrent() {
            service = Executors.newFixedThreadPool(4);
            latch = new CountDownLatch(4);
        }

        @BeforeEach
        void setUpData() {
            userRepository.save(user);
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            itemRepository.save(item);
            orderRepository.save(order);
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
            Delivery delivery = new Delivery(order);
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
