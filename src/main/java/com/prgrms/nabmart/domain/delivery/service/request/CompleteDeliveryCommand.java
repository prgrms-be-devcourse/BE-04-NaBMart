package com.prgrms.nabmart.domain.delivery.service.request;

public record CompleteDeliveryCommand(Long deliveryId, Long riderId) {

    public static CompleteDeliveryCommand of(final Long deliveryId, Long riderId) {
        return new CompleteDeliveryCommand(deliveryId, riderId);
    }
}
