package com.prgrms.nabmart.domain.delivery.service.request;

public record FindDeliveryCommand(Long userId, Long orderId) {

    public static FindDeliveryCommand of(final Long userId,final Long orderId) {
        return new FindDeliveryCommand(userId, orderId);
    }
}
