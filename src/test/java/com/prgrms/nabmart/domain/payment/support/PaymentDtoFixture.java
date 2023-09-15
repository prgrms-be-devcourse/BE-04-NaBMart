package com.prgrms.nabmart.domain.payment.support;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import com.prgrms.nabmart.domain.payment.service.response.PaymentResponse;

public class PaymentDtoFixture {

    public static PaymentRequestResponse paymentRequestResponse(Order order,
        String successCallBackUrl,
        String failCallBackUrl) {
        return new PaymentRequestResponse(
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
        return new PaymentResponse("SUCCESS", null);
    }

    public static PaymentResponse paymentResponseWithFail() {
        return new PaymentResponse("FAIL", "errorMessage");
    }
}
