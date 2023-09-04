package com.prgrms.nabmart.domain.event.controller.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RegisterEventItemsRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Nested
    @DisplayName("RegisterEventItemsRequest 필드 유효성 검증")
    class ValidationTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            RegisterEventItemsRequest request = new RegisterEventItemsRequest(Arrays.asList(1L, 2L));

            // When
            Set<ConstraintViolation<RegisterEventItemsRequest>> violations = validator.validate(
                request);

            // Then
            Assertions.assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("예외: 빈 아이템 리스트 요청")
        public void throwExceptionWhenItemListIsEmpty() {
            // Given
            RegisterEventItemsRequest request = new RegisterEventItemsRequest(new ArrayList<>());

            // When
            Set<ConstraintViolation<RegisterEventItemsRequest>> violations = validator.validate(
                request);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("아이템은 필수 입력 항목입니다.");
        }

        @Test
        @DisplayName("예외: 아이템 리스트 null 요청")
        public void throwExceptionWhenItemListIsNull() {
            // Given
            RegisterEventItemsRequest request = new RegisterEventItemsRequest(null);

            // When
            Set<ConstraintViolation<RegisterEventItemsRequest>> violations = validator.validate(
                request);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("아이템은 필수 입력 항목입니다.");
        }
    }
}
