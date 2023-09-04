package com.prgrms.nabmart.domain.payment.service.request;

import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.controller.request.PaymentRequest;
import jakarta.validation.constraints.NotBlank;

public record PaymentCommand (
        @NotBlank(message = "결제 타입은 필수 항목입니다.")
        PaymentType paymentType
) {

    public static PaymentCommand from(PaymentRequest paymentRequest) {
        return new PaymentCommand(
                paymentRequest.paymentType()
        );
    }
}
