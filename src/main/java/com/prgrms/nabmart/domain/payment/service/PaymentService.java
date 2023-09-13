package com.prgrms.nabmart.domain.payment.service;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.exception.NotPayingOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.payment.Payment;
import com.prgrms.nabmart.domain.payment.PaymentStatus;
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
import com.prgrms.nabmart.global.infrastructure.ApiService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ApiService apiService;

    @Value("${payment.toss.success-url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail-url}")
    private String failCallBackUrl;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    @Value("${payment.toss.confirm-url}")
    private String confirmUrl;

    @Transactional
    public PaymentRequestResponse pay(final PaymentCommand paymentCommand) {
        final Order order = getOrderByOrderIdAndUserId(
            paymentCommand.orderId(),
            paymentCommand.userId()
        );

        validateOrderStatusWithPending(order);
        order.changeStatus(OrderStatus.PAYING);
        order.redeemCoupon();

        final Payment payment = buildPayment(paymentCommand, order);
        paymentRepository.save(payment);

        return new PaymentRequestResponse(
            paymentCommand.paymentType(),
            order.getPrice(),
            order.getUuid(),
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

    private Order getOrderByUuidAndUserId(String uuid, Long userId) {
        return orderRepository.findByUuidAndUser_UserId(uuid, userId)
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
            .build();
    }

    @Transactional
    public PaymentResponse confirmPayment(
        Long userId,
        String uuid,
        String paymentKey,
        Integer amount
    ) {
        Payment payment = getPaymentByUuidAndUserId(uuid, userId);
        validatePayment(amount, payment);

        Order order = getOrderByUuidAndUserId(uuid, userId);
        validateOrderStatusWithPaying(order);

        HttpHeaders httpHeaders = getHttpHeaders();
        JSONObject params = getParams(uuid, paymentKey, amount);

        TossPaymentApiResponse paymentApiResponse = requestPaymentApi(httpHeaders, params);
        validatePaymentResult(payment, paymentApiResponse);

        payment.changeStatus(PaymentStatus.SUCCESS);
        payment.setPaymentKey(paymentKey);

        order.changeStatus(OrderStatus.PAYED);

        return new PaymentResponse(payment.getPaymentStatus().toString());
    }

    private void validateOrderStatusWithPaying(final Order order) {
        if (order.isMisMatchStatus(OrderStatus.PAYING)) {
            throw new NotPayingOrderException("결제가 진행중인 주문이 아닙니다.");
        }
    }

    private TossPaymentApiResponse requestPaymentApi(HttpHeaders httpHeaders, JSONObject params) {
        return apiService.getResult(
            new HttpEntity<>(params, httpHeaders),
            confirmUrl,
            TossPaymentApiResponse.class
        );
    }

    private void validatePayment(Integer amount, Payment payment) {
        validatePaymentStatus(payment);
        validatePrice(amount, payment);
    }

    private void validatePaymentStatus(final Payment payment) {
        if (payment.isMisMatchStatus(PaymentStatus.PENDING)) {
            throw new DuplicatePayException("이미 처리된 결제입니다.");
        }
    }

    private void validatePaymentResult(Payment payment, TossPaymentApiResponse paymentApiResponse) {
        if (payment.isMisMachType(paymentApiResponse.method())) {
            throw new PaymentTypeMismatchException("결제 타입이 일치하지 않습니다.");
        }

        if (!paymentApiResponse.status().equals("DONE")) {
            throw new PaymentFailException("결제가 실패되었습니다.");
        }
    }

    private JSONObject getParams(String uuid, String paymentKey, Integer amount) {
        JSONObject params = new JSONObject();
        params.put("paymentKey", paymentKey);
        params.put("orderId", uuid);
        params.put("amount", amount);

        return params;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(getEncodeAuth());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }

    private void validatePrice(Integer amount, Payment payment) {
        if (payment.isMisMatchPrice(amount)) {
            throw new PaymentAmountMismatchException("결제 금액이 일치하지 않습니다.");
        }
    }

    private String getEncodeAuth() {
        return new String(
            Base64.getEncoder()
                .encode((secretKey + ":").getBytes(StandardCharsets.UTF_8))
        );
    }

    private Payment getPaymentByUuidAndUserId(String uuid, Long userId) {
        return paymentRepository.findByOrder_UuidAndUser_UserId(uuid, userId)
            .orElseThrow(() -> new NotFoundPaymentException("결제가 존재하지 않습니다."));
    }
}
