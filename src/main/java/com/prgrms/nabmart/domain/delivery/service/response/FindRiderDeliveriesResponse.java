package com.prgrms.nabmart.domain.delivery.service.response;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import java.time.LocalDateTime;
import java.util.List;

public record FindRiderDeliveriesResponse(
    List<FindRiderDeliveryResponse> deliveries,
    int page,
    long totalElements) {

    public static FindRiderDeliveriesResponse of(
        final List<Delivery> content,
        final int page,
        final long totalElements) {
        List<FindRiderDeliveryResponse> deliveries = content.stream()
            .map(FindRiderDeliveryResponse::from)
            .toList();
        return new FindRiderDeliveriesResponse(
            deliveries,
            page,
            totalElements);
    }

    public record FindRiderDeliveryResponse(
        Long deliveryId,
        DeliveryStatus deliveryStatus,
        LocalDateTime arrivedAt,
        String address,
        int deliveryFee) {

        public static FindRiderDeliveryResponse from(final Delivery delivery) {
            return new FindRiderDeliveryResponse(
                delivery.getDeliveryId(),
                delivery.getDeliveryStatus(),
                delivery.getArrivedAt(),
                delivery.getOrder().getAddress(),
                delivery.getOrder().getDeliveryFee());
        }
    }
}
