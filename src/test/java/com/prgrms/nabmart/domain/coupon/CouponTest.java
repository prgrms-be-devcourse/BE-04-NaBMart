package com.prgrms.nabmart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.nabmart.domain.coupon.exception.InvalidCouponException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Nested
    @DisplayName("Coupon 생성 시")
    class newCouponTest {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            String name = "TestName";
            // When
            Coupon coupon = Coupon.builder()
                .name(name)
                .discount(10000)
                .description("TestDescription")
                .minOrderPrice(1000)
                .endAt(LocalDate.parse("2023-12-31"))
                .build();

            // Then
            assertThat(coupon.getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("예외 : 현재시간 보다 이전 만료일")
        void throwExceptionWhenInvalidEndAt() {
            // Given
            LocalDate endAt = LocalDate.parse("2021-12-31");

            // When && Then
            assertThatThrownBy(() -> Coupon.builder()
                .name("TestName")
                .discount(10000)
                .description("TestDescription")
                .minOrderPrice(1000)
                .endAt(endAt)
                .build())
                .isInstanceOf(InvalidCouponException.class)
                .hasMessage("쿠폰 종료일은 현재 날짜보다 이전일 수 없습니다.");
        }
    }
}

