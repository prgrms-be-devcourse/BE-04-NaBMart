package com.prgrms.nabmart.domain.payment.support;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.controller.request.PaymentRequest;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import com.prgrms.nabmart.domain.payment.service.response.PaymentResponse;

public class PaymentDtoFixture {

    private static final String PAYMENT_TYPE_CARD = PaymentType.CARD.toString();

    public static PaymentRequestResponse paymentRequestResponse(Order order,
        String successCallBackUrl,
        String failCallBackUrl) {
        return new PaymentRequestResponse(
            PAYMENT_TYPE_CARD,
            order.getPrice(),
            order.getUuid(),
            order.getName(),
            order.getUser().getEmail(),
            order.getUser().getNickname(),
            successCallBackUrl,
            failCallBackUrl
        );
    }

    public static PaymentResponse paymentResponseWithSuccess() {
        return new PaymentResponse("SUCCESS");
    }

    public static PaymentResponse paymentResponseWithFail() {
        return new PaymentResponse("FAIL");
    }

    public static PaymentCommand paymentCommandWithCard(Long orderId, Long userId) {
        return PaymentCommand.of(orderId, userId, PAYMENT_TYPE_CARD);
    }

    public static PaymentRequest paymentRequestWithCard() {
        return new PaymentRequest(PAYMENT_TYPE_CARD);
    }
}
