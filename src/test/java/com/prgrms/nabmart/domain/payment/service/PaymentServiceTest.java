package com.prgrms.nabmart.domain.payment.service;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.deliveringOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.payingOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.pendingOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.pendingOrderWithCoupon;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentCommandWithCard;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentRequestResponse;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentResponseWithSuccess;
import static com.prgrms.nabmart.domain.payment.support.PaymentFixture.canceledPayment;
import static com.prgrms.nabmart.domain.payment.support.PaymentFixture.pendingPayment;
import static com.prgrms.nabmart.domain.user.support.UserFixture.userWithUserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.coupon.exception.InvalidUsedCouponException;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.exception.NotPayingOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.exception.DuplicatePayException;
import com.prgrms.nabmart.domain.payment.exception.NotFoundPaymentException;
import com.prgrms.nabmart.domain.payment.exception.PaymentAmountMismatchException;
import com.prgrms.nabmart.domain.payment.exception.PaymentFailException;
import com.prgrms.nabmart.domain.payment.exception.PaymentTypeMismatchException;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import com.prgrms.nabmart.domain.payment.service.response.PaymentResponse;
import com.prgrms.nabmart.domain.payment.service.response.TossPaymentApiResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.global.infrastructure.ApiService;
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

    @Mock
    ApiService apiService;

    @Value("${payment.toss.success_url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail_url}")
    private String failCallBackUrl;

    @Value("${payment.toss.confirm-url}")
    private String confirmUrl;

    @Nested
    @DisplayName("pay 메서드 실행 시")
    class payTest {

        @Test
        @DisplayName("성공: 유효한 order 일 경우, PaymentResponse 를 반환")
        void pay() {
            // given
            User user = userWithUserId();
            Order order = pendingOrder(1L, user);

            PaymentCommand paymentCommand = paymentCommandWithCard(order.getOrderId(),
                user.getUserId());
            PaymentRequestResponse expected = paymentRequestResponse(order, successCallBackUrl,
                failCallBackUrl);

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            PaymentRequestResponse result = paymentService.pay(paymentCommand);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
            verify(paymentRepository, times(1)).save(any());

        }

        @Test
        @DisplayName("예외: order 가 존재하지 않을 경우, NotFoundOrderException 발생")
        void throwExceptionWhenInvalidOrder() {
            // given
            User user = userWithUserId();
            long noExistOrderId = 1L;

            PaymentCommand paymentCommand = new PaymentCommand(noExistOrderId, user.getUserId(),
                PaymentType.CARD.toString());

            when(orderRepository.findByOrderIdAndUser_UserId(noExistOrderId, user.getUserId()))
                .thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> paymentService.pay(paymentCommand));

            // then
            assertThat(exception).isInstanceOf(NotFoundOrderException.class);
        }

        @Test
        @DisplayName("예외: order 가 Pending 상태가 아닐 경우, DuplicatePayException 발생")
        void throwExceptionWhenNotPendingOrder() {
            // given
            User user = userWithUserId();
            Order order = deliveringOrder(1L, user);

            PaymentCommand paymentCommand = new PaymentCommand(order.getOrderId(), user.getUserId(),
                PaymentType.CARD.toString());

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            Exception exception = catchException(
                () -> paymentService.pay(paymentCommand));

            // then
            assertThat(exception).isInstanceOf(DuplicatePayException.class);
        }

        @Test
        @DisplayName("예외: coupon 이 이미 사용되었을 경우, AlreadyUsedCouponException 발생")
        void throwExceptionWhenAlreadyUsedCoupon() {
            // given
            User user = userWithUserId();

            Order order = pendingOrderWithCoupon(1L, user);
            order.redeemCoupon();

            PaymentCommand paymentCommand = new PaymentCommand(order.getOrderId(), user.getUserId(),
                PaymentType.CARD.toString());

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            Exception exception = catchException(
                () -> paymentService.pay(paymentCommand));

            // then
            assertThat(exception).isInstanceOf(InvalidUsedCouponException.class);
        }
    }

    @Nested
    @DisplayName("confirmPayment 메서드 실행 시")
    class ConfirmPaymentTest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            User user = userWithUserId();
            Order order = payingOrder(1L, user);
            Payment payment = pendingPayment(user, order);
            String mockPaymentKey = "mockPaymentKey";
            int amount = order.getPrice();
            PaymentResponse expected = paymentResponseWithSuccess();

            when(paymentRepository.findByOrder_OrderIdAndUser_UserId(order.getOrderId(),
                user.getUserId()))
                .thenReturn(Optional.of(payment));

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            when(apiService.getResult(any(), any(), any())).thenReturn(
                    new TossPaymentApiResponse("카드", "DONE"))
                .thenReturn(true);

            // when
            PaymentResponse result = paymentService.confirmPayment(user.getUserId(),
                order.getOrderId(), mockPaymentKey,
                amount);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        }

        @Test
        @DisplayName("예외: 결제가 존재하지 않을 경우, NotFoundPaymentException 발생")
        void throwExceptionWhenNotFoundPayment() {
            // given
            User user = userWithUserId();
            Order order = payingOrder(1L, user);
            String mockPaymentKey = "mockPaymentKey";
            int amount = order.getPrice();

            when(paymentRepository.findByOrder_OrderIdAndUser_UserId(order.getOrderId(),
                user.getUserId()))
                .thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> paymentService.confirmPayment(user.getUserId(),
                    order.getOrderId(), mockPaymentKey,
                    amount));

            // then
            assertThat(exception).isInstanceOf(NotFoundPaymentException.class);

        }

        @Test
        @DisplayName("예외: 결제가 Pending 상태가 아닐 경우, DuplicatePayException 발생")
        void throwDuplicatePayException() {
            // given
            User user = userWithUserId();
            Order order = payingOrder(1L, user);
            Payment canceledPayment = canceledPayment(user, order);
            String mockPaymentKey = "mockPaymentKey";
            int amount = order.getPrice();

            when(paymentRepository.findByOrder_OrderIdAndUser_UserId(order.getOrderId(),
                user.getUserId()))
                .thenReturn(Optional.of(canceledPayment));

            // when
            Exception exception = catchException(
                () -> paymentService.confirmPayment(user.getUserId(),
                    order.getOrderId(), mockPaymentKey,
                    amount));

            // then
            assertThat(exception).isInstanceOf(DuplicatePayException.class);
        }

        @Test
        @DisplayName("예외: 결제 금액이 일치하지 않는 경우, PaymentAmountMismatchException 발생")
        void throwExceptionWhenMismatchPayAmount() {
            // given
            User user = userWithUserId();
            Order order = payingOrder(1L, user);
            Payment payment = pendingPayment(user, order);
            String mockPaymentKey = "mockPaymentKey";
            int amount = 123;

            when(paymentRepository.findByOrder_OrderIdAndUser_UserId(order.getOrderId(),
                user.getUserId()))
                .thenReturn(Optional.of(payment));

            // when
            Exception exception = catchException(
                () -> paymentService.confirmPayment(user.getUserId(),
                    order.getOrderId(), mockPaymentKey,
                    amount));

            // then
            assertThat(exception).isInstanceOf(PaymentAmountMismatchException.class);
        }

        @Test
        @DisplayName("예외: 주문이 Paying 상태가 아닐 경우, NotPayingOrderException 발생")
        void throwExceptionWhenNotPayingOrder() {
            // given
            User user = userWithUserId();
            Order order = pendingOrder(1L, user);
            Payment payment = pendingPayment(user, order);
            String mockPaymentKey = "mockPaymentKey";
            int amount = order.getPrice();

            when(paymentRepository.findByOrder_OrderIdAndUser_UserId(order.getOrderId(),
                user.getUserId()))
                .thenReturn(Optional.of(payment));

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            // when
            Exception exception = catchException(
                () -> paymentService.confirmPayment(user.getUserId(),
                    order.getOrderId(), mockPaymentKey,
                    amount));

            // then
            assertThat(exception).isInstanceOf(NotPayingOrderException.class);
        }

        @Test
        @DisplayName("예외: 결제 타입이 일치하지 않는 경우, PaymentTypeMismatchException 발생")
        void throwExceptionWhenMismatchPaymentType() {
            // given
            User user = userWithUserId();
            Order order = payingOrder(1L, user);
            Payment payment = pendingPayment(user, order);
            String mockPaymentKey = "mockPaymentKey";
            int amount = order.getPrice();

            when(paymentRepository.findByOrder_OrderIdAndUser_UserId(order.getOrderId(),
                user.getUserId()))
                .thenReturn(Optional.of(payment));

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            when(apiService.getResult(any(), any(), any())).thenReturn(
                    new TossPaymentApiResponse("계좌이체", "DONE"))
                .thenReturn(true);

            // when
            Exception exception = catchException(
                () -> paymentService.confirmPayment(user.getUserId(),
                    order.getOrderId(), mockPaymentKey,
                    amount));

            // then
            assertThat(exception).isInstanceOf(PaymentTypeMismatchException.class);
        }

        @Test
        @DisplayName("예외: 결제 상태가 DONE 이 아닌 경우, PaymentFailException 발생")
        void throwExceptionWhenPayStatusIsNotDone() {
            // given
            User user = userWithUserId();
            Order order = payingOrder(1L, user);
            Payment payment = pendingPayment(user, order);
            String mockPaymentKey = "mockPaymentKey";
            int amount = order.getPrice();

            when(paymentRepository.findByOrder_OrderIdAndUser_UserId(order.getOrderId(),
                user.getUserId()))
                .thenReturn(Optional.of(payment));

            when(orderRepository.findByOrderIdAndUser_UserId(order.getOrderId(), user.getUserId()))
                .thenReturn(Optional.of(order));

            when(apiService.getResult(any(), any(), any())).thenReturn(
                    new TossPaymentApiResponse("카드", "ABORTED"))
                .thenReturn(true);

            // when
            Exception exception = catchException(
                () -> paymentService.confirmPayment(user.getUserId(),
                    order.getOrderId(), mockPaymentKey,
                    amount));

            // then
            assertThat(exception).isInstanceOf(PaymentFailException.class);
        }
    }
}
