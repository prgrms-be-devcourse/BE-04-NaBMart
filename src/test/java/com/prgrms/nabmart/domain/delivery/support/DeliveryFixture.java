package com.prgrms.nabmart.domain.delivery.support;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse.FindWaitingDeliveryResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DeliveryFixture {

    private static final Long DELIVERY_ID = 1L;
    private static final String ADDRESS = "주소지";
    private static final int DELIVERY_FEE = 3000;
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final DeliveryStatus DELIVERY_STATUS = DeliveryStatus.ACCEPTING_ORDER;
    private static final Long USER_ID = 1L;
    private static final Long ORDER_ID = 1L;
    private static final String ORDER_NAME = "비비고 왕교자 외 2개";
    private static final int ORDER_PRICE = 1000;
    private static final String RIDER_REQUEST = "문 앞에 두고 벨 눌러주세요.";
    private static final int ESTIMATE_MINUTES = 20;
    private static final int PAGE = 0;
    private static final String RIDER_USERNAME = "username";
    private static final String RIDER_PASSWORD = "password123";
    private static final String RIDER_ADDRESS = "address";

    public static Delivery waitingDelivery(Order order) {
        order.updateOrderStatus(OrderStatus.PAYED);
        return Delivery.builder()
            .order(order)
            .build();
    }

    public static Delivery acceptedDelivery(Order order, Rider rider) {
        Delivery delivery = waitingDelivery(order);
        delivery.assignRider(rider);
        return delivery;
    }

    public static Delivery completedDelivery(Order order, Rider rider) {
        Delivery delivery = acceptedDelivery(order, rider);
        delivery.completeDelivery();
        return delivery;
    }

    public static FindDeliveryCommand findDeliveryCommand() {
        return new FindDeliveryCommand(USER_ID, ORDER_ID);
    }

    public static FindDeliveryDetailResponse findDeliveryDetailResponse() {
        return new FindDeliveryDetailResponse(
            DELIVERY_ID,
            DELIVERY_STATUS,
            NOW,
            NOW,
            ORDER_ID,
            ORDER_NAME,
            ORDER_PRICE,
            RIDER_REQUEST);
    }

    public static FindWaitingDeliveriesResponse findDeliveriesResponse() {
        FindWaitingDeliveryResponse findWaitingDeliveryResponse
            = new FindWaitingDeliveryResponse(DELIVERY_ID, NOW, NOW, ADDRESS, DELIVERY_FEE);
        return new FindWaitingDeliveriesResponse(List.of(findWaitingDeliveryResponse), PAGE, 1);
    }

    public static Rider rider() {
        return new Rider(RIDER_USERNAME, RIDER_PASSWORD, RIDER_ADDRESS);
    }
}
