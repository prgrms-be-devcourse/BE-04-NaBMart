package com.prgrms.nabmart.domain.payment.controller.request;

import com.prgrms.nabmart.domain.payment.PaymentType;
import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(
        @NotBlank(message = "결제 타입은 필수 항목입니다.")
        PaymentType paymentType
) {
}
