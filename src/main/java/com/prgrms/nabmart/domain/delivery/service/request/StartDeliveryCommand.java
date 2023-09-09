package com.prgrms.nabmart.domain.delivery.service.request;

public record StartDeliveryCommand(
    Long deliveryId,
    int deliveryEstimateMinutes) {

    public static StartDeliveryCommand of(
        final Long deliveryId,
        final Integer deliveryEstimateMinutes) {
        return new StartDeliveryCommand(deliveryId, deliveryEstimateMinutes);
    }
}
