package com.prgrms.nabmart.domain.delivery.support;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.order.Order;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DeliveryFixture {

    private static final Long USER_ID = 1L;
    private static final Long ORDER_ID = 1L;
    private static final String ADDRESS = "주소지";
    private static final LocalDateTime FINISHED_TIME = LocalDateTime.now();

    public static Delivery delivery(Order order) {
        return Delivery.builder()
            .order(order)
            .address(ADDRESS)
            .finishedTime(FINISHED_TIME)
            .build();
    }

    public static FindDeliveryCommand findDeliveryCommand() {
        return new FindDeliveryCommand(USER_ID, ORDER_ID);
    }

}
