package com.prgrms.nabmart.domain.review.controller.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.review.support.UpdateReviewRequestFixture;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateReviewRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    @DisplayName("UpdateReviewRequest 생성 시")
    class ValidateUpdateReviewRequest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            UpdateReviewRequest registerReviewRequest = UpdateReviewRequestFixture.updateReviewRequest();

            // when
            Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(0);
        }

        @Test
        @DisplayName("예외 : rate 가 0미만인 경우")
        void throwExceptionWhenRateIsNull() {
            // given
            UpdateReviewRequest registerReviewRequest = new UpdateReviewRequest(
                -1, "내공 냠냠"
            );

            // when
            Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : rate 가 5점 초과")
        void throwExceptionWhenRateIsMoreThanMaxLength() {
            // given
            UpdateReviewRequest registerReviewRequest = new UpdateReviewRequest(
                6, "내공 냠냠"
            );

            // when
            Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : content 가 null")
        void throwExceptionWhenContentIsNull() {
            // given
            UpdateReviewRequest registerReviewRequest = new UpdateReviewRequest(
                5, null
            );

            // when
            Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("예외 : content 가 빈 칸인 경우")
        void throwExceptionWhenContentIsBlank() {
            // given
            UpdateReviewRequest registerReviewRequest = new UpdateReviewRequest(
                5, ""
            );

            // when
            Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(2);
        }

        @Test
        @DisplayName("예외 : content 가 100자 초과인 경우")
        void throwExceptionWhenContentIsMoreThanMaxLength() {
            // given
            UpdateReviewRequest registerReviewRequest = new UpdateReviewRequest(
                5, "z".repeat(101)
            );

            // when
            Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(
                registerReviewRequest);

            // then
            assertThat(violations).hasSize(1);
        }
    }
}