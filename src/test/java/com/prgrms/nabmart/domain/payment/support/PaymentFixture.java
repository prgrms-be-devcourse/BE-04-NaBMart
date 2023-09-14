package com.prgrms.nabmart.domain.payment.support;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.PaymentStatus;
import com.prgrms.nabmart.domain.user.User;
import org.springframework.test.util.ReflectionTestUtils;

public class PaymentFixture {

    public static Payment pendingPayment(User user, Order order) {
        return Payment.builder()
            .user(user)
            .order(order)
            .build();
    }

    public static Payment canceledPayment(User user, Order order) {
        Payment payment = Payment.builder()
            .user(user)
            .order(order)
            .build();
        ReflectionTestUtils.setField(payment, "paymentStatus", PaymentStatus.CANCELED);

        return payment;
    }

    public static Payment successPayment(User user, Order order) {
        Payment payment = pendingPayment(user, order);
        ReflectionTestUtils.setField(payment, "paymentStatus", PaymentStatus.SUCCESS);
        return payment;
    }
}
