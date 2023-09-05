package com.prgrms.nabmart.domain.payment.service.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentCommand (
        @NotBlank(message = "결제 타입은 필수 항목입니다.")
        String paymentType
) {

    public static PaymentCommand from(String paymentType) {
        return new PaymentCommand(paymentType);
    }
}
