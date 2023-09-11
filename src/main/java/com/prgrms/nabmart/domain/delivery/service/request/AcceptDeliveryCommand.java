package com.prgrms.nabmart.domain.delivery.service.request;

public record AcceptDeliveryCommand(Long deliveryId, Long riderId) {

    public static AcceptDeliveryCommand of(Long deliveryId, Long riderId) {
        return new AcceptDeliveryCommand(deliveryId, riderId);
    }
}
