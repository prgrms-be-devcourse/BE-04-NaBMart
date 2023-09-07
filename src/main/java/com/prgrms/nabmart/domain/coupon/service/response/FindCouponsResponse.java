package com.prgrms.nabmart.domain.coupon.service.response;

import com.prgrms.nabmart.domain.coupon.Coupon;
import java.util.List;
import java.util.stream.Collectors;

public record FindCouponsResponse(List<FindCouponResponse> coupons) {

    public static FindCouponsResponse from(final List<Coupon> coupons) {
        return coupons.stream()
            .map(FindCouponResponse::from)
            .collect(Collectors.collectingAndThen(Collectors.toList(), FindCouponsResponse::new));
    }

}
