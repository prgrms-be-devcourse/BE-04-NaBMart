package com.prgrms.nabmart.domain.payment.service;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.payingOrder;
import static com.prgrms.nabmart.domain.user.support.UserFixture.userWithUserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.payment.exception.PaymentFailException;
import com.prgrms.nabmart.domain.payment.service.response.TossPaymentApiResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.global.infrastructure.ApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentClientTest {

    @InjectMocks
    PaymentClient paymentClient;

    @Mock
    ApiService apiService;

    @Test
    @DisplayName("예외: 결제 상태가 DONE 이 아닌 경우, PaymentFailException 발생")
    void throwExceptionWhenPayStatusIsNotDone() {
        // given
        User user = userWithUserId();
        Order order = payingOrder(1L, user);

        String mockPaymentKey = "mockPaymentKey";
        int amount = order.getPrice();

        when(apiService.getResult(any(), any(), any()))
            .thenReturn(new TossPaymentApiResponse("간편결제", "FAIL"));

        // when
        Exception exception = catchException(
            () -> paymentClient.confirmPayment(order.getUuid(), mockPaymentKey,
                amount));

        // then
        assertThat(exception).isInstanceOf(PaymentFailException.class);
    }

}
