package com.prgrms.nabmart.domain.cart.controller.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterCartItemRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    @DisplayName("RegisterCartItemRequest 생성 시")
    class ValidateRegisterCartItemRequest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            RegisterCartItemRequest givenRegisterCartItemRequest = new RegisterCartItemRequest(1L,
                5);

            // when
            Set<ConstraintViolation<RegisterCartItemRequest>> violations = validator.validate(
                givenRegisterCartItemRequest);

            // then
            assertThat(violations).hasSize(0);
        }

        @Test
        @DisplayName("예외 : itemId가 null")
        void throwExceptionWhenItemIdIsNull() {
            // given
            RegisterCartItemRequest givenRegisterCartItemRequest = new RegisterCartItemRequest(null,
                5);

            // when
            Set<ConstraintViolation<RegisterCartItemRequest>> violations = validator.validate(
                givenRegisterCartItemRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : itemId가 양수가 아닌 경우")
        void throwExceptionWhenItemIdIsNotPositive() {
            // given
            RegisterCartItemRequest givenRegisterCartItemRequest = new RegisterCartItemRequest(-1L,
                5);

            // when
            Set<ConstraintViolation<RegisterCartItemRequest>> violations = validator.validate(
                givenRegisterCartItemRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : Quantity 가 null")
        void throwExceptionWhenQuantityIsNull() {
            // given
            RegisterCartItemRequest givenRegisterCartItemRequest = new RegisterCartItemRequest(1L,
                null);

            // when
            Set<ConstraintViolation<RegisterCartItemRequest>> violations = validator.validate(
                givenRegisterCartItemRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : Quantity 가 양수가 아닌 경우")
        void throwExceptionWhenQuantityIsNotPositive() {
            // given
            RegisterCartItemRequest givenRegisterCartItemRequest = new RegisterCartItemRequest(1L,
                -1);

            // when
            Set<ConstraintViolation<RegisterCartItemRequest>> violations = validator.validate(
                givenRegisterCartItemRequest);

            // then
            assertThat(violations).hasSize(1);
        }
    }
}
