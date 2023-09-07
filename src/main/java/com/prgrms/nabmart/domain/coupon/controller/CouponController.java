package com.prgrms.nabmart.domain.coupon.controller;

import com.prgrms.nabmart.domain.coupon.controller.request.RegisterCouponRequest;
import com.prgrms.nabmart.domain.coupon.exception.CouponException;
import com.prgrms.nabmart.domain.coupon.service.CouponService;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterUserCouponCommand;
import com.prgrms.nabmart.domain.coupon.service.response.FindCouponsResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        RegisterCouponCommand registerCouponCommand = RegisterCouponCommand.from(
            registerCouponRequest);
        Long couponId = couponService.createCoupon(registerCouponCommand);
        URI location = URI.create("/api/v1/coupons/" + couponId);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/my-coupons/{couponId}")
    public ResponseEntity<Void> RegisterUserCoupon(
        @PathVariable final Long couponId,
        @LoginUser final Long userId
    ) {
        RegisterUserCouponCommand registerUserCouponCommand = RegisterUserCouponCommand.of(userId,
            couponId);
        Long userCouponId = couponService.registerUserCoupon(registerUserCouponCommand);
        URI location = URI.create("/api/v1/my-coupons/" + userCouponId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/coupons")
    public ResponseEntity<FindCouponsResponse> findCoupons() {
        FindCouponsResponse findCouponsResponse = couponService.findCoupons();
        return ResponseEntity.ok(findCouponsResponse);
    }

    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final CouponException couponException) {
        log.info(couponException.getMessage());
        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(couponException.getMessage()));
    }
}
