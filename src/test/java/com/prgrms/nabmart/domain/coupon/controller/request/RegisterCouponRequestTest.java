package com.prgrms.nabmart.domain.coupon.controller.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterCouponRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private final Validator validator = factory.getValidator();

    @Nested
    @DisplayName("RegisterCouponRequest 필드 유효성 검증")
    class ValidationTest {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            RegisterCouponRequest request = new RegisterCouponRequest("ValidName", 10000,
                "ValidDescription", 1000, LocalDate.parse("2023-12-31"));
            // When
            Set<ConstraintViolation<RegisterCouponRequest>> violations = validator.validate(
                request);

            // Then
            Assertions.assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("예외 : 쿠폰명 빈 문자열 요청")
        public void throwExceptionWhenCouponNameIsBlank() {
            // Given
            RegisterCouponRequest request = new RegisterCouponRequest("", 10000, "ValidDescription",
                1000, LocalDate.parse("2023-12-31"));

            // When
            Set<ConstraintViolation<RegisterCouponRequest>> violations = validator.validate(
                request);

            // Then
            Assertions.assertThat(violations).hasSize(1).extracting(ConstraintViolation::getMessage)
                .containsOnly("쿠폰 이름은 필수 입력 항목입니다.");
        }

        @Test
        @DisplayName("예외 : 할인 금액 음수 값 요청")
        public void throwExceptionWhenDiscountIsNegative() {
            // Given
            RegisterCouponRequest request = new RegisterCouponRequest("ValidName", -10000,
                "ValidDescription", 1000, LocalDate.parse("2023-12-31"));

            // When
            Set<ConstraintViolation<RegisterCouponRequest>> violations = validator.validate(
                request);

            //Then
            Assertions.assertThat(violations).hasSize(1).extracting(ConstraintViolation::getMessage)
                .containsOnly("할인 금액은 양수이어야 합니다.");
        }

        @Test
        @DisplayName("예외: 할인 금액 null 요청")
        public void throwExceptionWhenDiscountIsNull() {
            // Given
            RegisterCouponRequest request = new RegisterCouponRequest("ValidName", null,
                "ValidDescription", 1000, LocalDate.parse("2023-12-31"));

            // When
            Set<ConstraintViolation<RegisterCouponRequest>> violations = validator.validate(
                request);

            //Then
            Assertions.assertThat(violations).hasSize(1).extracting(ConstraintViolation::getMessage)
                .containsOnly("할인 금액은 필수 입력 항목입니다.");
        }

        @Test
        @DisplayName("예외 : 최소 주문 금액 음수 값 요청")
        public void throwExceptionWhenMinOrderPriceIsNegative() {
            // Given
            RegisterCouponRequest request = new RegisterCouponRequest("ValidName", 10000,
                "ValidDescription", -1000, LocalDate.parse("2023-12-31"));

            // When
            Set<ConstraintViolation<RegisterCouponRequest>> violations = validator.validate(
                request);

            //Then
            Assertions.assertThat(violations).hasSize(1).extracting(ConstraintViolation::getMessage)
                .containsOnly("최소 주문 금액은 0 이상이어야 합니다.");
        }

        @Test
        @DisplayName("예외 : 최소 주문 금액 null 요청")
        public void throwExceptionWhenMinOrderPriceIsNull() {
            // Given
            RegisterCouponRequest request = new RegisterCouponRequest("ValidName", 10000,
                "ValidDescription", null, LocalDate.parse("2023-12-31"));

            // When
            Set<ConstraintViolation<RegisterCouponRequest>> violations = validator.validate(
                request);

            //Then
            Assertions.assertThat(violations).hasSize(1).extracting(ConstraintViolation::getMessage)
                .containsOnly("최소 주문 금액은 필수 입력 항목입니다.");
        }

        @Test
        @DisplayName("예외 : 쿠폰 만료일 null 요청")
        public void throwExceptionWhenEndAtIsNull() {
            // Given
            RegisterCouponRequest request = new RegisterCouponRequest("ValidName", 10000,
                "ValidDescription", 1000, null);

            // When
            Set<ConstraintViolation<RegisterCouponRequest>> violations = validator.validate(
                request);

            //Then
            Assertions.assertThat(violations).hasSize(1).extracting(ConstraintViolation::getMessage)
                .containsOnly("쿠폰 종료일은 필수 입력 항목입니다.");
        }
    }
}
