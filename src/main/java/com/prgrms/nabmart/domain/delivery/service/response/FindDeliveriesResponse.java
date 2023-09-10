package com.prgrms.nabmart.domain.delivery.service.response;

import com.prgrms.nabmart.domain.delivery.Delivery;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindDeliveriesResponse(
    List<FindDeliveryResponse> deliveries,
    int page,
    long totalElements) {

    public static FindDeliveriesResponse from(Page<Delivery> deliveries) {
        Page<FindDeliveryResponse> deliveryResponses
            = deliveries.map(FindDeliveryResponse::from);

        return new FindDeliveriesResponse(
            deliveryResponses.getContent(),
            deliveryResponses.getNumber(),
            deliveryResponses.getTotalElements());
    }

    public record FindDeliveryResponse(Long deliveryId, String address, int deliveryFee) {

        public static FindDeliveryResponse from(Delivery delivery) {
            return new FindDeliveryResponse(
                delivery.getDeliveryId(),
                delivery.getAddress(),
                delivery.getDeliveryFee());
        }
    }
}
