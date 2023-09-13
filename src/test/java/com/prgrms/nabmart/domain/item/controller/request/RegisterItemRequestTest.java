package com.prgrms.nabmart.domain.item.controller.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterItemRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Nested
    @DisplayName("RegisterItemRequestTest 필드 유효성 검증")
    class ValidationTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            RegisterItemRequest registerItemRequest = RegisterItemRequest.builder()
                .name("NAME")
                .price(100)
                .description("DESCRIPTION")
                .quantity(10)
                .discount(10)
                .maxBuyQuantity(10)
                .mainCategoryId(1L)
                .subCategoryId(1L)
                .build();

            // When
            Set<ConstraintViolation<RegisterItemRequest>> violations = validator.validate(
                registerItemRequest);

            // Then
            assertThat(violations).isEmpty();
        }


        @Test
        @DisplayName("상품명 누락 - 실패")
        public void emptyName() {
            // Given
            RegisterItemRequest request = RegisterItemRequest.builder()
                .name("")
                .price(100)
                .quantity(10)
                .discount(10)
                .maxBuyQuantity(10)
                .mainCategoryId(1L)
                .subCategoryId(1L)
                .build();

            // When
            Set<ConstraintViolation<RegisterItemRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message")
                .contains("상품명은 필수 항목입니다.");
        }

        @Test
        @DisplayName("상품 가격 음수 - 실패")
        public void negativePrice() {
            // Given
            RegisterItemRequest request = RegisterItemRequest.builder()
                .name("Product")
                .price(-100)
                .quantity(10)
                .discount(10)
                .maxBuyQuantity(10)
                .mainCategoryId(1L)
                .subCategoryId(1L)
                .build();

            // When
            Set<ConstraintViolation<RegisterItemRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message")
                .contains("상품 가격은 0원 이상이어야 합니다.");
        }

        @Test
        @DisplayName("상품 수량 음수 - 실패")
        public void negativeQuantity() {
            // Given
            RegisterItemRequest request = RegisterItemRequest.builder()
                .name("Product")
                .price(100)
                .quantity(-10) // 음수 수량은 유효하지 않음
                .discount(10)
                .maxBuyQuantity(10)
                .mainCategoryId(1L)
                .subCategoryId(1L)
                .build();

            // When
            Set<ConstraintViolation<RegisterItemRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message")
                .contains("상품 수량은 0개 이상이어야 합니다.");
        }

        @Test
        @DisplayName("할인율 101% - 실패")
        public void invalidDiscount() {
            // Given
            RegisterItemRequest request = RegisterItemRequest.builder()
                .name("Product")
                .price(100)
                .quantity(10)
                .discount(101) // 100을 초과하는 할인율은 유효하지 않음
                .maxBuyQuantity(10)
                .mainCategoryId(1L)
                .subCategoryId(1L)
                .build();

            // When
            Set<ConstraintViolation<RegisterItemRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message")
                .contains("상품 할인율은 0~100% 사이만 가능합니다.");
        }
    }
}
