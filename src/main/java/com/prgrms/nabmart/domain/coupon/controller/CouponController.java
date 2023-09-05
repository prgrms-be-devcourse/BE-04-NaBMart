package com.prgrms.nabmart.domain.coupon.controller;

import com.prgrms.nabmart.domain.coupon.controller.request.RegisterCouponRequest;
import com.prgrms.nabmart.domain.coupon.exception.CouponException;
import com.prgrms.nabmart.domain.coupon.service.CouponService;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<Void> createCoupon(
        @Valid @RequestBody RegisterCouponRequest registerCouponRequest) {
        RegisterCouponCommand command = RegisterCouponCommand.from(registerCouponRequest);
        Long couponId = couponService.createCoupon(command);
        URI location = URI.create("/api/v1/coupons" + "/" + couponId);
        return ResponseEntity.created(location).build();
    }

    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final CouponException couponException) {
        log.info(couponException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(couponException.getMessage()));
    }
}
