package com.prgrms.nabmart.domain.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.coupon.exception.InvalidCouponException;
import com.prgrms.nabmart.domain.coupon.exception.NotFoundCouponException;
import com.prgrms.nabmart.domain.coupon.repository.CouponRepository;
import com.prgrms.nabmart.domain.coupon.repository.UserCouponRepository;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterUserCouponCommand;
import com.prgrms.nabmart.domain.coupon.support.CouponFixture;
import com.prgrms.nabmart.domain.coupon.support.UserCouponFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    User givenUser;
    Coupon givenCoupon;
    UserCoupon givenUserCoupon;

    @BeforeEach
    void setUp() {
        givenUser = UserFixture.user();
        givenCoupon = CouponFixture.coupon();
        givenUserCoupon = UserCouponFixture.userCoupon(givenUser, givenCoupon);
    }

    @Nested
    @DisplayName("createCoupon 메서드 실행 시")
    class CreateCouponTests {

        RegisterCouponCommand registerCouponCommand = CouponFixture.registerCouponCommand();

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            Coupon coupon = givenCoupon;
            when(couponRepository.save(any(Coupon.class))).thenReturn(
                coupon);

            // When
            couponService.createCoupon(registerCouponCommand);

            // Then
            verify(couponRepository, times(1)).save(any(Coupon.class));
        }
    }

    @Nested
    @DisplayName("RegisterUserCoupon 메서드 실행 시")
    class RegisterCouponTests {

        RegisterUserCouponCommand registerUserCouponCommand = UserCouponFixture.registerUserCouponCommand();

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(givenUserCoupon);
            when(couponRepository.findById((any()))).thenReturn(Optional.ofNullable(givenCoupon));
            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(givenUser));
            when(userCouponRepository.existsByUserAndCoupon(any(User.class), any(Coupon.class)))
                .thenReturn(false);

            // When
            couponService.registerUserCoupon(registerUserCouponCommand);

            // Then
            verify(userCouponRepository, times(1)).save((any(UserCoupon.class)));
        }

        @Test
        @DisplayName("예외 : coupon 이 존재하지 않는 경우, NotFoundCouponException 발생")
        public void throwExceptionWhenNotFoundCoupon() {
            // Given
            when(couponRepository.findById((any()))).thenReturn(Optional.empty());
            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(givenUser));

            // When
            Exception exception = catchException(
                () -> couponService.registerUserCoupon(registerUserCouponCommand));

            // Then
            assertThat(exception).isInstanceOf(NotFoundCouponException.class);
        }

        @Test
        @DisplayName("예외 : 이미 발급한 coupon 발급 시, InvalidCouponException 발생")
        public void throwExceptionWhenAlreadyIssuedCoupon() {
            // Given
            when(couponRepository.findById((any()))).thenReturn(Optional.ofNullable(givenCoupon));
            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(givenUser));
            when(userCouponRepository.existsByUserAndCoupon(any(User.class), any(Coupon.class)))
                .thenReturn(true);

            // When
            Exception exception = catchException(
                () -> couponService.registerUserCoupon(registerUserCouponCommand));

            // Then
            assertThat(exception).isInstanceOf(InvalidCouponException.class);
            assertThat(exception.getMessage()).isEqualTo("이미 발급받은 쿠폰입니다.");
        }

        @Test
        @DisplayName("예외 : 만료된 coupon 발급 시 , InvalidCouponException 발생")
        public void throwExceptionWhenExpirationCoupon() {
            // Given
            LocalDate expiredDate = LocalDate.now().minusDays(1); // 이미 만료된 날짜 설정
            ReflectionTestUtils.setField(givenCoupon, "endAt", expiredDate); // 쿠폰의 만료일을 변경

            when(couponRepository.findById(any())).thenReturn(Optional.ofNullable(givenCoupon));
            when(userRepository.findById(any())).thenReturn(Optional.ofNullable(givenUser));

            // When
            Exception exception = catchException(
                () -> couponService.registerUserCoupon(registerUserCouponCommand));

            // Then
            assertThat(exception).isInstanceOf(InvalidCouponException.class);
            assertThat(exception.getMessage()).isEqualTo("쿠폰이 이미 만료되었습니다");
        }
    }
}
