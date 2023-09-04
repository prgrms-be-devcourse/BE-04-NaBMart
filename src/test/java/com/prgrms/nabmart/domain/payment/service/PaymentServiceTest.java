package com.prgrms.nabmart.domain.payment.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.controller.response.PaymentResponse;
import com.prgrms.nabmart.domain.payment.exception.DuplicatePayException;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.getDeliveringOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.getPendingOrder;
import static com.prgrms.nabmart.domain.user.support.UserFixture.getUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
            long orderId = 1L;
            User user = getUser();
            Order order = getPendingOrder(orderId, user);

            PaymentCommand paymentCommand = new PaymentCommand(PaymentType.CARD.toString());

            PaymentResponse expected = new PaymentResponse(
                    PaymentType.CARD.toString(),
                    order.getPrice(),
                    orderId,
                    order.getName(),
                    order.getUser().getEmail(),
                    order.getUser().getNickname(),
                    successCallBackUrl,
                    failCallBackUrl
            );

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when
            PaymentResponse result = paymentService.pay(orderId, paymentCommand);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
            verify(paymentRepository).save(any());
        }

        @Test
        @DisplayName("실패: order 가 존재하지 않을 경우, NotFoundOrderException 발생")
        void throwExceptionWhenInvalidOrder() {
            // given
            long noExistOrderId = 1L;

            PaymentCommand paymentCommand = new PaymentCommand(PaymentType.CARD.toString());

            when(orderRepository.findById(noExistOrderId)).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> paymentService.pay(noExistOrderId, paymentCommand));

            // then
            assertThat(exception).isInstanceOf(NotFoundOrderException.class);
        }

        @Test
        @DisplayName("실패: order 가 Pending 상태가 아닐 경우, DuplicatePayException 발생")
        void throwExceptionWhenNotPendingOrder() {
            // given
            long orderId = 1L;
            User user = getUser();
            Order order = getDeliveringOrder(orderId, user);

            PaymentCommand paymentCommand = new PaymentCommand(PaymentType.CARD.toString());

            PaymentResponse expected = new PaymentResponse(
                    PaymentType.CARD.toString(),
                    order.getPrice(),
                    orderId,
                    order.getName(),
                    order.getUser().getEmail(),
                    order.getUser().getNickname(),
                    successCallBackUrl,
                    failCallBackUrl
            );

            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // when
            Exception exception = catchException(() -> paymentService.pay(orderId, paymentCommand));

            // then
            assertThat(exception).isInstanceOf(DuplicatePayException.class);
        }
    }
}
