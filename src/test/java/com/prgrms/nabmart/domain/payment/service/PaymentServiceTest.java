package com.prgrms.nabmart.domain.payment.service;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.deliveringOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.pendingOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.pendingOrderWithCoupon;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentCommandWithCard;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.coupon.exception.AlreadyUsedCouponException;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.exception.DuplicatePayException;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderRepository orderRepository;

    @Value("${payment.toss.success_url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail_url}")
    private String failCallBackUrl;

    @Nested
    @DisplayName("pay 메서드 실행 시")
    class payTest {

        @Test
        @DisplayName("성공: 유효한 order 일 경우, PaymentResponse 를 반환")
        void pay() {
            // given
            User user = UserFixture.user();
            Order order = pendingOrder(1L, user);

            PaymentCommand paymentCommand = paymentCommandWithCard();
            PaymentRequestResponse expected = paymentResponse(order, successCallBackUrl,
                failCallBackUrl);

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            PaymentRequestResponse result = paymentService.pay(order.getOrderId(), user.getUserId(),
                paymentCommand);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
            verify(paymentRepository, times(1)).save(any());

        }

        @Test
        @DisplayName("예외: order 가 존재하지 않을 경우, NotFoundOrderException 발생")
        void throwExceptionWhenInvalidOrder() {
            // given
            User user = UserFixture.user();
            long noExistOrderId = 1L;

            PaymentCommand paymentCommand = new PaymentCommand(PaymentType.CARD.toString());

            when(orderRepository.findByOrderIdAndUser_UserId(noExistOrderId, user.getUserId()))
                .thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> paymentService.pay(noExistOrderId, user.getUserId(), paymentCommand));

            // then
            assertThat(exception).isInstanceOf(NotFoundOrderException.class);
        }

        @Test
        @DisplayName("예외: order 가 Pending 상태가 아닐 경우, DuplicatePayException 발생")
        void throwExceptionWhenNotPendingOrder() {
            // given
            User user = UserFixture.user();
            Order order = deliveringOrder(1L, user);

            PaymentCommand paymentCommand = new PaymentCommand(PaymentType.CARD.toString());

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            Exception exception = catchException(
                () -> paymentService.pay(order.getOrderId(), user.getUserId(), paymentCommand));

            // then
            assertThat(exception).isInstanceOf(DuplicatePayException.class);
        }

        @Test
        @DisplayName("예외: coupon 이 이미 사용되었을 경우, AlreadyUsedCouponException 발생")
        void throwExceptionWhenAlreadyUsedCoupon() {
            // given
            User user = UserFixture.user();

            Order order = pendingOrderWithCoupon(1L, user);
            order.redeemCoupon();

            PaymentCommand paymentCommand = new PaymentCommand(PaymentType.CARD.toString());

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            Exception exception = catchException(
                () -> paymentService.pay(order.getOrderId(), user.getUserId(), paymentCommand));

            // then
            assertThat(exception).isInstanceOf(AlreadyUsedCouponException.class);
        }
    }
}
