package com.prgrms.nabmart.domain.payment.service.response;

public record PaymentRequestResponse(
    String paymentType,
    Integer amount,
    String orderId,
    String orderName,
    String customerEmail,
    String customerName,
    String successUrl,
    String failUrl
) {

}
