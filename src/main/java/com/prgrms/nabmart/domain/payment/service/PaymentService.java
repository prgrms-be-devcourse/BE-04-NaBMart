package com.prgrms.nabmart.domain.payment.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NoExistsOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.PaymentStatus;
import com.prgrms.nabmart.domain.payment.controller.response.PaymentResponse;
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

    private final static String MOCK_ORDER_NAME = "orderName";

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    @Value("${payment.toss.success_url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail_url}")
    String failCallBackUrl;

    @Transactional
    public PaymentResponse pay(Long orderId, PaymentCommand paymentCommand) {
        final Order order = getOrderByOrderId(orderId);
        final Payment payment = buildPayment(paymentCommand, order);

        paymentRepository.save(payment);

        return new PaymentResponse(
                paymentCommand.paymentType().toString(),
                order.getPrice(),
                String.valueOf(order.getOrderId()),
                MOCK_ORDER_NAME,
                order.getUser().getEmail(),
                order.getUser().getNickname(),
                successCallBackUrl,
                failCallBackUrl);
        }

    private Payment buildPayment(PaymentCommand paymentCommand, Order order) {
        return Payment.builder()
                .order(order)
                .user(order.getUser())
                .paymentType(paymentCommand.paymentType())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

    private Order getOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoExistsOrderException("order 가 존재하지 않습니다."));
    }
}
