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
        List<Delivery> content,
        int number,
        long totalElements) {
        List<FindRiderDeliveryResponse> deliveries = content.stream()
            .map(FindRiderDeliveryResponse::from)
            .toList();
        return new FindRiderDeliveriesResponse(deliveries, number, totalElements);
    }

    public record FindRiderDeliveryResponse(
        Long deliveryId,
        DeliveryStatus deliveryStatus,
        LocalDateTime arrivedAt,
        String address,
        int deliveryFee) {

        public static FindRiderDeliveryResponse from(Delivery delivery) {
            return new FindRiderDeliveryResponse(
                delivery.getDeliveryId(),
                delivery.getDeliveryStatus(),
                delivery.getArrivedAt(),
                delivery.getOrder().getAddress(),
                delivery.getOrder().getDeliveryFee()
            );
        }
    }
}
