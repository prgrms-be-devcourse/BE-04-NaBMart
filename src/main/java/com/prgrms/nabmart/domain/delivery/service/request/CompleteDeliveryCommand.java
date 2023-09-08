package com.prgrms.nabmart.domain.delivery.service.request;

public record CompleteDeliveryCommand(Long deliveryId) {

    public static CompleteDeliveryCommand of(final Long deliveryId) {
        return new CompleteDeliveryCommand(deliveryId);
    }
}
