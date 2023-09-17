package com.prgrms.nabmart.domain.delivery.service.response;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import java.time.LocalDateTime;

public record FindDeliveryDetailResponse(
    Long deliveryId,
    DeliveryStatus deliveryStatus,
    LocalDateTime createdAt,
    LocalDateTime arrivedAt,
    Long orderId,
    String orderName,
    int orderPrice,
    String riderRequest) {

    public static FindDeliveryDetailResponse from(final Delivery delivery) {
        return new FindDeliveryDetailResponse(
            delivery.getDeliveryId(),
            delivery.getDeliveryStatus(),
            delivery.getCreatedAt(),
            delivery.getArrivedAt(),
            delivery.getOrder().getOrderId(),
            delivery.getOrder().getName(),
            delivery.getOrderPrice(),
            delivery.getRiderRequest());
    }
}
