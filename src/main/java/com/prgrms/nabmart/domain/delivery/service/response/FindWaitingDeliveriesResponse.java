package com.prgrms.nabmart.domain.delivery.service.response;

import com.prgrms.nabmart.domain.delivery.Delivery;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindWaitingDeliveriesResponse(
    List<FindWaitingDeliveryResponse> deliveries,
    int page,
    long totalElements) {

    public static FindWaitingDeliveriesResponse from(final Page<Delivery> deliveries) {
        Page<FindWaitingDeliveryResponse> deliveryResponses
            = deliveries.map(FindWaitingDeliveryResponse::from);

        return new FindWaitingDeliveriesResponse(
            deliveryResponses.getContent(),
            deliveryResponses.getNumber(),
            deliveryResponses.getTotalElements());
    }

    public record FindWaitingDeliveryResponse(Long deliveryId, String address, int deliveryFee) {

        public static FindWaitingDeliveryResponse from(final Delivery delivery) {
            return new FindWaitingDeliveryResponse(
                delivery.getDeliveryId(),
                delivery.getAddress(),
                delivery.getDeliveryFee());
        }
    }
}
