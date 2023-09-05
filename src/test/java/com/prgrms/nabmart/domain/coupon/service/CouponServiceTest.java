package com.prgrms.nabmart.domain.coupon.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.repository.CouponRepository;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Nested
    @DisplayName("createCoupon 메서드 실행 시")
    class CreateCouponTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            RegisterCouponCommand command = new RegisterCouponCommand("TestName", 10000,
                "TestDescription", 1000, LocalDate.parse("2023-12-31"));
            Coupon coupon = Coupon.builder()
                .name(command.name())
                .discount(command.discount())
                .description(command.description())
                .minOrderPrice(command.minOrderPrice())
                .endAt(command.endAt())
                .build();
            when(couponRepository.save(any(Coupon.class))).thenReturn(
                coupon);

            // When
            couponService.createCoupon(command);

            // Then
            verify(couponRepository, times(1)).save(any(Coupon.class));
        }
    }
}