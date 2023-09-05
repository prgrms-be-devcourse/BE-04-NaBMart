package com.prgrms.nabmart.domain.payment.controller.request;

import com.prgrms.nabmart.domain.payment.PaymentType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PaymentRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    @DisplayName("PaymentRequest 생성 시")
    class validatePaymentRequest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            String paymentType = PaymentType.CARD.toString();
            PaymentRequest paymentRequest = new PaymentRequest(paymentType);

            // when
            Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(
                paymentRequest);

            // then
            Assertions.assertThat(violations).hasSize(0);
        }

        @Test
        @DisplayName("실패: paymentType 이 null 일 경우, 예외 발생")
        void throwExceptionWhenPaymentTypeIsNull() {
            // given
            String nullPaymentType = null;
            PaymentRequest paymentRequest = new PaymentRequest(nullPaymentType);

            // when
            Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(
                paymentRequest);

            // then
            Assertions.assertThat(violations).hasSize(1);
        }
    }
}
