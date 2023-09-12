package com.prgrms.nabmart.domain.payment.service.request;

public record PaymentCommand(
    Long orderId,
    Long userId,
    String paymentType
) {

    public static PaymentCommand of(Long orderId, Long userId, String paymentType) {
        return new PaymentCommand(orderId, userId, paymentType);
    }
}
