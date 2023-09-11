package com.prgrms.nabmart.domain.delivery;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.deliveringOrder;
import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeliveryTest {

    @Nested
    @DisplayName("Delivery 생성 시")
    class NewDeliveryTest {

        User user = UserFixture.user();
        Order order = deliveringOrder(1L, user);

        @Test
        @DisplayName("성공")
        void success() {
            //given
            String address = "주소지";

            //when
            Delivery delivery = Delivery.builder()
                .order(order)
                .build();

            //then
            assertThat(delivery.getOrder()).isEqualTo(order);
        }
    }
}
