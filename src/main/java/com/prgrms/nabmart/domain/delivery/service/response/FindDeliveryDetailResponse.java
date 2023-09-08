package com.prgrms.nabmart.domain.delivery.service.response;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import java.time.LocalDateTime;

public record FindDeliveryDetailResponse(
    Long deliveryId,
    DeliveryStatus deliveryStatus,
    LocalDateTime arriveTime,
    String address,
    Long orderId,
    String name,
    int price) {

    public static FindDeliveryDetailResponse from(final Delivery delivery) {
        return new FindDeliveryDetailResponse(
            delivery.getDeliveryId(),
            delivery.getDeliveryStatus(),
            delivery.getArriveTime(),
            delivery.getAddress(),
            delivery.getOrder().getOrderId(),
            delivery.getOrder().getName(),
            delivery.getOrder().getPrice());
    }
}
