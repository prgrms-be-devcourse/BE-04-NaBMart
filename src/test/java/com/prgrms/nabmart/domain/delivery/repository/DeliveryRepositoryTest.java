package com.prgrms.nabmart.domain.delivery.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.support.DeliveryFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.order.support.OrderFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class DeliveryRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    User user = UserFixture.user();
    Order order = OrderFixture.getDeliveringOrder(1L, user);

    @BeforeEach
    void init() {
        userRepository.save(user);
        orderRepository.save(order);
    }

    private List<Order> createAndSaveOrders(int end) {
        List<Order> orders = IntStream.range(0, end)
            .mapToObj(i -> Order.builder()
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
            .map(DeliveryFixture::delivery)
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
}