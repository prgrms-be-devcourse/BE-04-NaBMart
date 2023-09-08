package com.prgrms.nabmart.domain.delivery.service.request;

public record CompleteDeliveryCommand(Long deliveryId) {

    public static CompleteDeliveryCommand from(final Long deliveryId) {
        return new CompleteDeliveryCommand(deliveryId);
    }
}
