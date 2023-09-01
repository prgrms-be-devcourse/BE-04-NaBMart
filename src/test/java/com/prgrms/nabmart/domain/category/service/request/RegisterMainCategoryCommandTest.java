package com.prgrms.nabmart.domain.category.service.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RegisterMainCategoryCommandTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Nested
    @DisplayName("RegisterMainCategoryCommand 필드 유효성 검증")
    class ValidationTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            RegisterMainCategoryCommand command = new RegisterMainCategoryCommand("ValidName");

            // When
            Set<ConstraintViolation<RegisterMainCategoryCommand>> violations = validator.validate(
                command);

            // Then
            Assertions.assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("예외: 카테고리명 빈 문자열 요청")
        public void throwExceptionWhenCategoryNameIsBlank() {
            // Given
            RegisterMainCategoryCommand command = new RegisterMainCategoryCommand("");

            // When
            Set<ConstraintViolation<RegisterMainCategoryCommand>> violations = validator.validate(
                command);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("대카테고리명은 필수 항목입니다.");
        }

        @Test
        @DisplayName("예외: 카테고리명 null 요청")
        public void throwExceptionWhenCategoryNameIsNull() {
            // Given
            RegisterMainCategoryCommand command = new RegisterMainCategoryCommand(null);

            // When
            Set<ConstraintViolation<RegisterMainCategoryCommand>> violations = validator.validate(
                command);

            // Then
            Assertions.assertThat(violations)
                .hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("대카테고리명은 필수 항목입니다.");
        }
    }
}
