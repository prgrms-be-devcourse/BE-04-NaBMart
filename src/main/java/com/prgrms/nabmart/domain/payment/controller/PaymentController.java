package com.prgrms.nabmart.domain.payment.controller;

import com.prgrms.nabmart.domain.payment.controller.request.PaymentRequest;
import com.prgrms.nabmart.domain.payment.service.PaymentService;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import com.prgrms.nabmart.domain.payment.service.response.PaymentResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pays")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentRequestResponse> pay(
        @RequestBody @Valid PaymentRequest paymentRequest,
        @PathVariable Long orderId,
        @LoginUser Long userId
    ) {
        PaymentCommand paymentCommand = PaymentCommand.of(orderId, userId,
            paymentRequest.paymentType());
        return ResponseEntity.ok(paymentService.pay(paymentCommand));
    }

    @GetMapping("/toss/success")
    public ResponseEntity<PaymentResponse> paySuccess(
        @RequestParam("orderId") String uuid,
        @RequestParam("paymentKey") String paymentKey,
        @RequestParam("amount") Integer amount,
        @LoginUser Long userId
    ) {
        return ResponseEntity.ok(
            paymentService.confirmPayment(userId, uuid, paymentKey, amount));
    }
}
