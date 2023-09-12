package com.prgrms.nabmart.domain.delivery.controller.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindRiderDeliveriesRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Nested
    @DisplayName("deliveryStatus 검증 시")
    class DeliveryStatusTest {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            FindRiderDeliveriesRequest findRiderDeliveriesRequest
                = new FindRiderDeliveriesRequest(DeliveryStatus.ACCEPTING_ORDER);

            //when
            Set<ConstraintViolation<FindRiderDeliveriesRequest>> result
                = validator.validate(findRiderDeliveriesRequest);

            //then
            assertThat(result).hasSize(0);
        }

        @Test
        @DisplayName("예외: 배달 상태가 null")
        void throwExceptionWhenDeliveryStatusIsNull() {
            //given
            FindRiderDeliveriesRequest findRiderDeliveriesRequest
                = new FindRiderDeliveriesRequest(null);

            //when
            Set<ConstraintViolation<FindRiderDeliveriesRequest>> result
                = validator.validate(findRiderDeliveriesRequest);

            //then
            assertThat(result).hasSize(1);
            assertThat(result).map(ConstraintViolation::getInvalidValue).containsOnlyNulls();
        }
    }
}