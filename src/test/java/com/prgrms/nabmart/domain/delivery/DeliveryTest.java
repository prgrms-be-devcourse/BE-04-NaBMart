package com.prgrms.nabmart.domain.delivery;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.nabmart.domain.delivery.exception.InvalidDeliveryException;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.support.OrderFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeliveryTest {

    @Nested
    @DisplayName("Delivery 생성 시")
    class NewDeliveryTest {

        User user = UserFixture.user();
        Order order = OrderFixture.getDeliveringOrder(1L, user);

        @Test
        @DisplayName("예외: 주소 길이가 500자를 초과")
        void throwExceptionWhenInvalidAddressLength() {
            //given
            String invalidAddress = "a".repeat(501);

            //when
            //then
            assertThatThrownBy(() ->
                    Delivery.builder()
                        .order(order)
                        .finishedTime(LocalDateTime.now())
                        .address(invalidAddress)
                        .build())
                .isInstanceOf(InvalidDeliveryException.class);
        }
    }
}