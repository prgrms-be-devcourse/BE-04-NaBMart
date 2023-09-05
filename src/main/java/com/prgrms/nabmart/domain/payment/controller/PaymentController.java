package com.prgrms.nabmart.domain.payment.controller;

import com.prgrms.nabmart.domain.payment.controller.request.PaymentRequest;
import com.prgrms.nabmart.domain.payment.controller.response.PaymentResponse;
import com.prgrms.nabmart.domain.payment.service.PaymentService;
import com.prgrms.nabmart.domain.payment.service.request.PaymentCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pays")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> pay(
            @RequestBody @Valid PaymentRequest paymentRequest,
            @PathVariable Long orderId
    ) {
        PaymentCommand paymentCommand = PaymentCommand.from(paymentRequest.paymentType());
        return ResponseEntity.ok(paymentService.pay(orderId, paymentCommand));
    }
}
