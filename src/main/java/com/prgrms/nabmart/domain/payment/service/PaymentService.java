package com.prgrms.nabmart.domain.payment.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.exception.NotPayingOrderException;
import com.prgrms.nabmart.domain.order.service.OrderService;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.PaymentStatus;
import com.prgrms.nabmart.domain.payment.exception.DuplicatePayException;
import com.prgrms.nabmart.domain.payment.exception.NotFoundPaymentException;
import com.prgrms.nabmart.domain.payment.exception.PaymentAmountMismatchException;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import com.prgrms.nabmart.domain.payment.service.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Value("${payment.toss.success-url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail-url}")
    private String failCallBackUrl;

    @Transactional
    public PaymentRequestResponse pay(final Long orderId, final Long userId) {
        final Order order = getOrderByOrderIdAndUserId(
            orderId,
            userId
        );

        validateOrderStatusWithPending(order);
        order.changeStatus(OrderStatus.PAYING);
        order.useCoupon();

        final Payment payment = buildPayment(order);
        paymentRepository.save(payment);

        return new PaymentRequestResponse(
            order.getPrice(),
            order.getUuid(),
            order.getName(),
            order.getUser().getEmail(),
            order.getUser().getNickname(),
            successCallBackUrl,
            failCallBackUrl);
    }


    private Order getOrderByOrderIdAndUserId(Long orderId, Long userId) {
        return orderService.getOrderByOrderIdAndUserId(orderId, userId);
    }

    private Order getOrderByUuidAndUserId(String uuid, Long userId) {
        return orderService.getOrderByUuidAndUserId(uuid, userId);
    }

    private void validateOrderStatusWithPending(final Order order) {
        if (order.isMisMatchStatus(OrderStatus.PENDING)) {
            throw new DuplicatePayException("이미 처리된 주문입니다.");
        }
    }

    private Payment buildPayment(Order order) {
        return Payment.builder()
            .order(order)
            .user(order.getUser())
            .build();
    }

    @Transactional
    public PaymentResponse processSuccessPayment(
        Long userId,
        String uuid,
        String paymentKey,
        Integer amount
    ) {
        Payment payment = getPaymentByUuidAndUserId(uuid, userId);
        validatePayment(amount, payment);

        Order order = getOrderByUuidAndUserId(uuid, userId);
        validateOrderStatusWithPaying(order);

        payment.changeStatus(PaymentStatus.SUCCESS);
        payment.setPaymentKey(paymentKey);

        order.changeStatus(OrderStatus.PAYED);

        return new PaymentResponse(payment.getPaymentStatus().toString(), null);
    }

    private void validateOrderStatusWithPaying(final Order order) {
        if (order.isMisMatchStatus(OrderStatus.PAYING)) {
            throw new NotPayingOrderException("결제가 진행중인 주문이 아닙니다.");
        }
    }

    private void validatePayment(Integer amount, Payment payment) {
        validatePaymentStatusWithPending(payment);
        validatePrice(amount, payment);
    }

    private void validatePaymentStatusWithPending(final Payment payment) {
        if (payment.isMisMatchStatus(PaymentStatus.PENDING)) {
            throw new DuplicatePayException("이미 처리된 결제입니다.");
        }
    }

    private void validatePrice(Integer amount, Payment payment) {
        if (payment.isMisMatchPrice(amount)) {
            throw new PaymentAmountMismatchException("결제 금액이 일치하지 않습니다.");
        }
    }

    private Payment getPaymentByUuidAndUserId(String uuid, Long userId) {
        return paymentRepository.findByOrder_UuidAndUser_UserId(uuid, userId)
            .orElseThrow(() -> new NotFoundPaymentException("결제가 존재하지 않습니다."));
    }

    @Transactional
    public PaymentResponse processFailPayment(Long userId, String uuid, String errorMessage) {
        Payment payment = getPaymentByUuidAndUserId(uuid, userId);
        validatePaymentStatusWithPending(payment);
        payment.changeStatus(PaymentStatus.FAILED);

        Order order = getOrderByUuidAndUserId(uuid, userId);
        validateOrderStatusWithPaying(order);

        orderService.cancelOrder(order);

        return new PaymentResponse(payment.getPaymentStatus().toString(), errorMessage);
    }
}
