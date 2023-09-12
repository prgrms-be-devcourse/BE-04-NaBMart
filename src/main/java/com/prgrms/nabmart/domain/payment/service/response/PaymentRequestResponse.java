package com.prgrms.nabmart.domain.payment.service.response;

public record PaymentRequestResponse(
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
