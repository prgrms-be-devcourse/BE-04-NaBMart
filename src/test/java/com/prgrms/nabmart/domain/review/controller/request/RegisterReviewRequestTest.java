package com.prgrms.nabmart.domain.review.controller.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.review.support.RegisterReviewRequestFixture;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterReviewRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    @DisplayName("RegisterReviewRequestTest 생성 시")
    class ValidateRegisterReviewRequest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            RegisterReviewRequest registerReviewRequest = RegisterReviewRequestFixture.registerReviewRequest();

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(0);
        }

        @Test
        @DisplayName("예외 : itemId 가 null")
        void throwExceptionWhenItemIdIsNull() {
            // given
            RegisterReviewRequest registerReviewRequest = new RegisterReviewRequest(
                null, 5, "내공 냠냠"
            );

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : itemId 가 양수가 아닌 경우")
        void throwExceptionWhenItemIdIsNotPositive() {
            // given
            RegisterReviewRequest registerReviewRequest = new RegisterReviewRequest(
                -1L, 5, "내공 냠냠"
            );

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : rate 가 0미만인 경우")
        void throwExceptionWhenRateIsMinus() {
            // given
            RegisterReviewRequest registerReviewRequest = new RegisterReviewRequest(
                1L, -1, "내공 냠냠"
            );

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : rate 가 5점 초과")
        void throwExceptionWhenRateIsMoreThanMaxLength() {
            // given
            RegisterReviewRequest registerReviewRequest = new RegisterReviewRequest(
                1L, 6, "내공 냠냠"
            );

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : content 가 null")
        void throwExceptionWhenContentIsNull() {
            // given
            RegisterReviewRequest registerReviewRequest = new RegisterReviewRequest(
                1L, 5, null
            );

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : content 가 빈 칸인 경우")
        void throwExceptionWhenContentIsBlank() {
            // given
            RegisterReviewRequest registerReviewRequest = new RegisterReviewRequest(
                1L, 5, ""
            );

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(2);
        }

        @Test
        @DisplayName("예외 : content 가 100자 초과인 경우")
        void throwExceptionWhenContentIsMoreThanMaxLength() {
            // given
            RegisterReviewRequest registerReviewRequest = new RegisterReviewRequest(
                1L, 5, "1".repeat(101)
            );

            // when
            Set<ConstraintViolation<RegisterReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }
    }
}
