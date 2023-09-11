package com.prgrms.nabmart.domain.payment.support;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.controller.request.PaymentRequest;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;

public class PaymentDtoFixture {

    private static String PAYMENT_TYPE_CARD = PaymentType.CARD.toString();

    public static PaymentRequestResponse paymentResponse(Order order, String successCallBackUrl,
        String failCallBackUrl) {
        return new PaymentRequestResponse(
            PAYMENT_TYPE_CARD,
            order.getPrice(),
            order.getOrderId(),
            order.getName(),
            order.getUser().getEmail(),
            order.getUser().getNickname(),
            successCallBackUrl,
            failCallBackUrl
        );
    }

    public static PaymentCommand paymentCommandWithCard() {
        return PaymentCommand.from(PAYMENT_TYPE_CARD);
    }

    public static PaymentRequest paymentRequestWithCard() {
        return new PaymentRequest(PAYMENT_TYPE_CARD);
    }
}
