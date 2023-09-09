package com.prgrms.nabmart.domain.delivery.support;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.order.Order;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DeliveryFixture {

    private static final Long DELIVERY_ID = 1L;
    private static final String ADDRESS = "주소지";
    private static final int DELIVERY_FEE = 3000;
    private static final LocalDateTime ARRIVED_AT = LocalDateTime.now();
    private static final DeliveryStatus DELIVERY_STATUS = DeliveryStatus.ACCEPTING_ORDER;
    private static final Long USER_ID = 1L;
    private static final Long ORDER_ID = 1L;
    private static final String ORDER_NAME = "비비고 왕교자 외 2개";
    private static final int ORDER_PRICE = 1000;
    private static final int ESTIMATE_MINUTES = 20;

    public static Delivery delivery(Order order) {
        return Delivery.builder()
            .order(order)
            .address(ADDRESS)
            .deliveryFee(DELIVERY_FEE)
            .build();
    }

    public static FindDeliveryCommand findDeliveryCommand() {
        return new FindDeliveryCommand(USER_ID, ORDER_ID);
    }

    public static FindDeliveryDetailResponse findDeliveryDetailResponse() {
        return new FindDeliveryDetailResponse(
            DELIVERY_ID,
            DELIVERY_STATUS,
            ARRIVED_AT,
            ADDRESS,
            DELIVERY_FEE,
            ORDER_ID,
            ORDER_NAME,
            ORDER_PRICE
        );
    }
}
