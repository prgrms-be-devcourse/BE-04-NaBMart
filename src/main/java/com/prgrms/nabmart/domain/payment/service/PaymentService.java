package com.prgrms.nabmart.domain.payment.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.PaymentStatus;
import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.exception.DuplicatePayException;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${payment.toss.success-url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail-url}")
    private String failCallBackUrl;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    @Value("${payment.toss.confirm-url}")
    private String confirmUrl;

    @Transactional
    public PaymentRequestResponse pay(final Long orderId,
        final Long userId,
        final PaymentCommand paymentCommand
    ) {
        final Order order = getOrderByOrderIdAndUserId(orderId, userId);
        validateOrderStatusWithPending(order);
        order.changeStatus(OrderStatus.PAYING);
        order.redeemCoupon();

        final Payment payment = buildPayment(paymentCommand, order);
        paymentRepository.save(payment);

        return new PaymentRequestResponse(
            paymentCommand.paymentType(),
            order.getPrice(),
            order.getOrderId(),
            order.getName(),
            order.getUser().getEmail(),
            order.getUser().getNickname(),
            successCallBackUrl,
            failCallBackUrl);
    }


    private Order getOrderByOrderIdAndUserId(Long orderId, Long userId) {
        return orderRepository.findByOrderIdAndUser_UserId(orderId, userId)
            .orElseThrow(() -> new NotFoundOrderException("주문 존재하지 않습니다."));
    }

    private void validateOrderStatusWithPending(final Order order) {
        if (order.isMisMatchStatus(OrderStatus.PENDING)) {
            throw new DuplicatePayException("이미 처리된 주문입니다.");
        }
    }

    private Payment buildPayment(PaymentCommand paymentCommand, Order order) {
        return Payment.builder()
            .order(order)
            .user(order.getUser())
            .paymentType(PaymentType.valueOf(paymentCommand.paymentType()))
            .paymentStatus(PaymentStatus.PENDING)
            .build();
    }

}
