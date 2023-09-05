package com.prgrms.nabmart.domain.coupon.service.request;

public record RegisterUserCouponCommand(
    Long userId,
    Long couponId
) {

    public static RegisterUserCouponCommand of(
        final Long userId,
        final Long couponId) {
        return new RegisterUserCouponCommand(userId, couponId);
    }
}
