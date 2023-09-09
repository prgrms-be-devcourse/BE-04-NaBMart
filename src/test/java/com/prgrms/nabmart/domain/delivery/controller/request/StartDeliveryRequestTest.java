package com.prgrms.nabmart.domain.delivery.controller.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StartDeliveryRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Nested
    @DisplayName("deliveryEstimateMinutes 유효성 검증 시")
    class DeliveryEstimateMinutesTest {

        @ParameterizedTest
        @CsvSource({
            "0", "1", "2", "5", "10"
        })
        @DisplayName("성공")
        void success(int validEstimateMinutes) {
            //given
            StartDeliveryRequest startDeliveryRequest
                = new StartDeliveryRequest(validEstimateMinutes);

            //when
            Set<ConstraintViolation<StartDeliveryRequest>> result
                = validator.validate(startDeliveryRequest);

            //then
            assertThat(result).isEmpty();
        }

        @ParameterizedTest
        @CsvSource({
            "-1", "-2", "-3", "-5", "-10"
        })
        @DisplayName("예외: 배달 예상 소요 시간이 음수")
        void throwExceptionWhenDeliveryEstimateMinutesIsMinus(int invalidEstimateMinutes) {
            //given
            StartDeliveryRequest startDeliveryRequest
                = new StartDeliveryRequest(invalidEstimateMinutes);

            //when
            Set<ConstraintViolation<StartDeliveryRequest>> result
                = validator.validate(startDeliveryRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).extracting(ConstraintViolation::getInvalidValue)
                .containsOnly(invalidEstimateMinutes);
        }

        @Test
        @DisplayName("예외: 배달 예상 소요 시간이 null")
        void throwExceptionWhenDeliveryEstimateMinutesIsNull() {
            //given
            StartDeliveryRequest startDeliveryRequest = new StartDeliveryRequest(null);

            //when
            Set<ConstraintViolation<StartDeliveryRequest>> result
                = validator.validate(startDeliveryRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).extracting(ConstraintViolation::getInvalidValue).containsNull();
        }
    }
}
