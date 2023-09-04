package com.prgrms.nabmart.domain.payment.controller.response;

public record PaymentResponse(
        String paymentType,
        Integer amount,
        Long orderId,
        String orderName,
        String customerEmail,
        String customerName,
        String successUrl,
        String failUrl
) {
}
