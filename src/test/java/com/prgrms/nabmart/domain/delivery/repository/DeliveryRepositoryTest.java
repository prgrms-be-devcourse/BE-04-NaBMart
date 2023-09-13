package com.prgrms.nabmart.domain.delivery.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.prgrms.nabmart.base.TestQueryDslConfig;
import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.support.DeliveryFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class DeliveryRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    RiderRepository riderRepository;

    User user = UserFixture.user();
    Rider rider = DeliveryFixture.rider();
    TemporalUnitOffset withInOneSeconds = within(1, ChronoUnit.SECONDS);

    @BeforeEach
    void init() {
        userRepository.save(user);
        riderRepository.save(rider);
    }

    private List<Order> createAndSaveOrders(int end) {
        List<Order> orders = IntStream.range(0, end)
            .mapToObj(i -> Order.builder()
                .uuid(UUID.randomUUID().toString())
                .price(1000)
                .name("비비고 왕교자 1개 외 2개")
                .user(user)
                .status(OrderStatus.COMPLETED)
                .build())
            .toList();
        orderRepository.saveAll(orders);
        return orders;
    }

    private List<Delivery> createAndSaveDeliveries(List<Order> orders) {
        List<Delivery> deliveries = orders.stream()
            .map(DeliveryFixture::waitingDelivery)
            .toList();
        deliveryRepository.saveAll(deliveries);
        return deliveries;
    }

    @Nested
    @DisplayName("findAllWaitingDeliveries 메서드 실행 시")
    class FindAllWaitingDeliveriesTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            int totalElements = 3;
            List<Order> orders = createAndSaveOrders(totalElements);
            List<Delivery> deliveries = createAndSaveDeliveries(orders);
            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Delivery> waitingDeliveriesWithPage
                = deliveryRepository.findWaitingDeliveries(pageRequest);

            //then
            assertThat(waitingDeliveriesWithPage.getTotalElements()).isEqualTo(totalElements);
            assertThat(waitingDeliveriesWithPage.getContent())
                .containsExactlyInAnyOrderElementsOf(deliveries);
        }
    }

    @Nested
    @DisplayName("findRiderDeliveries 메서드 실행 시")
    class FindRiderDeliveriesResponseTest {

        int estimateMinutes = 20;

        @BeforeEach
        void setUp() {
            List<Order> orders = createAndSaveOrders(9);
            List<Delivery> deliveries = createAndSaveDeliveries(orders);
            IntStream.range(0, 3)
                .forEach(i -> {
                    Delivery delivery = deliveries.get(i);
                    delivery.assignRider(rider);
                    delivery.startDelivery(estimateMinutes);
                });
            IntStream.range(3, 6)
                .forEach(i -> {
                    Delivery delivery = deliveries.get(i);
                    delivery.assignRider(rider);
                    delivery.startDelivery(estimateMinutes);
                    delivery.completeDelivery();
                });
            IntStream.range(6, 9)
                .forEach(i -> {
                    Delivery delivery = deliveries.get(i);
                    delivery.assignRider(rider);
                });
        }

        @Test
        @DisplayName("성공")
        void success() {
            //given
            List<DeliveryStatus> deliveryStatuses
                = List.of(DeliveryStatus.ACCEPTING_ORDER, DeliveryStatus.START_DELIVERY);
            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Delivery> deliveriesPage
                = deliveryRepository.findRiderDeliveries(rider, deliveryStatuses,  pageRequest);

            //then
            List<Delivery> findDeliveries = deliveriesPage.getContent();
            assertThat(findDeliveries).hasSize(6);
            assertThat(findDeliveries).map(Delivery::getDeliveryStatus)
                .contains(DeliveryStatus.ACCEPTING_ORDER, DeliveryStatus.START_DELIVERY)
                .doesNotContain(DeliveryStatus.DELIVERED);
        }
    }
}