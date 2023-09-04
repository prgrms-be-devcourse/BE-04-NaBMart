package com.prgrms.nabmart.domain.event.controller.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RegisterEventRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Nested
    @DisplayName("RegisterEventRequest 필드 유효성 검증")
    class ValidationTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            RegisterEventRequest request = new RegisterEventRequest("title", "description");

            // When
            Set<ConstraintViolation<RegisterEventRequest>> violations = validator.validate(request);

            // Then
            Assertions.assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("예외: 제목 빈 문자열 요청")
        public void throwExceptionWhenTitleIsBlank() {
            // Given
            RegisterEventRequest request = new RegisterEventRequest("", "description");

            // When
            Set<ConstraintViolation<RegisterEventRequest>> violations = validator.validate(request);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("이벤트 제목은 필수 입력 항목입니다.");
        }

        @Test
        @DisplayName("예외: 설명 빈 문자열 요청")
        public void throwExceptionWhenDescriptionIsBlank() {
            // Given
            RegisterEventRequest request = new RegisterEventRequest("title", "");

            // When
            Set<ConstraintViolation<RegisterEventRequest>> violations = validator.validate(request);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("이벤트 설명은 필수 입력 항목입니다.");
        }

        @Test
        @DisplayName("예외: 제목 null 요청")
        public void throwExceptionWhenTitleIsNull() {
            // Given
            RegisterEventRequest request = new RegisterEventRequest(null, "description");

            // When
            Set<ConstraintViolation<RegisterEventRequest>> violations = validator.validate(request);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("이벤트 제목은 필수 입력 항목입니다.");
        }

        @Test
        @DisplayName("예외: 설명 null 요청")
        public void throwExceptionWhenDescriptionIsNull() {
            // Given
            RegisterEventRequest request = new RegisterEventRequest("title", null);

            // When
            Set<ConstraintViolation<RegisterEventRequest>> violations = validator.validate(request);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("이벤트 설명은 필수 입력 항목입니다.");
        }
    }
}
