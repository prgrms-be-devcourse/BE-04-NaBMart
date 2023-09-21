package com.prgrms.nabmart.domain.delivery;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.payingOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.nabmart.domain.delivery.exception.InvalidDeliveryException;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeliveryTest {

    @Nested
    @DisplayName("Delivery 생성 시")
    class NewDeliveryTest {

        User user = UserFixture.user();
        Order order = payingOrder(1L, user);

        @BeforeEach
        void setUp() {
            order.updateOrderStatus(OrderStatus.PAYED);
        }

        @Test
        @DisplayName("성공")
        void success() {
            //given
            String address = "주소지";
            int estimateMinutes = 30;

            //when
            Delivery delivery = Delivery.builder()
                .order(order)
                .estimateMinutes(estimateMinutes)
                .build();

            //then
            assertThat(delivery.getOrder()).isEqualTo(order);
        }

        @Test
        @DisplayName("예외: 배송 예상 완료 시간이 음수")
        void throwExceptionWhenEstimateMinutesIsMinus() {
            //given
            int estimateMinutes = -1;

            //when
            //then
            assertThatThrownBy(() -> Delivery.builder()
                .order(order)
                .estimateMinutes(estimateMinutes)
                .build())
                .isInstanceOf(InvalidDeliveryException.class);
        }

        @Test
        @DisplayName("예외: 결제 완료된 주문이 아님")
        void throwExceptionWhenOrderIsNotPayed() {
            //given
            Order noPayedOrder = payingOrder(1L, user);

            //when
            //then
            assertThatThrownBy(() -> Delivery.builder()
                .order(noPayedOrder)
                .estimateMinutes(30)
                .build())
                .isInstanceOf(InvalidDeliveryException.class);
        }
    }
}
