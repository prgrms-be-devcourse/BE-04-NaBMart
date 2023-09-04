package com.prgrms.nabmart.domain.payment.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.PaymentStatus;
import com.prgrms.nabmart.domain.payment.PaymentType;
import com.prgrms.nabmart.domain.payment.controller.response.PaymentResponse;
import com.prgrms.nabmart.domain.payment.exception.DuplicatePayException;
import com.prgrms.nabmart.domain.payment.repository.PaymentRepository;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    @Value("${payment.toss.success_url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail_url}")
    String failCallBackUrl;

    @Transactional
    public PaymentResponse pay(Long orderId, PaymentCommand paymentCommand) {
        final Order order = getOrderByOrderId(orderId);

        validateOrder(order);

        final Payment payment = buildPayment(paymentCommand, order);
        paymentRepository.save(payment);

        return new PaymentResponse(
                paymentCommand.paymentType(),
                order.getPrice(),
                order.getOrderId(),
                order.getName(),
                order.getUser().getEmail(),
                order.getUser().getNickname(),
                successCallBackUrl,
                failCallBackUrl);
        }

    private void validateOrder(Order order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new DuplicatePayException("이미 결제가 완료된 order 입니다.");
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

    private Order getOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundOrderException("order 가 존재하지 않습니다."));
    }
}
