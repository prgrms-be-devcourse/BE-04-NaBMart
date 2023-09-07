package com.prgrms.nabmart.domain.coupon.service.response;

import com.prgrms.nabmart.domain.coupon.Coupon;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record FindCouponsResponse(List<FindCouponResponse> coupons) {

    public static FindCouponsResponse from(final List<Coupon> coupons) {
        return coupons.stream()
            .map(FindCouponResponse::from)
            .collect(Collectors.collectingAndThen(Collectors.toList(), FindCouponsResponse::new));
    }

    public record FindCouponResponse(
        Long couponId,
        String name,
        String description,
        Integer discount,
        Integer minOrderPrice,
        LocalDate endAt
    ) {

        public static FindCouponResponse from(final Coupon coupon) {
            return new FindCouponResponse(
                coupon.getCouponId(),
                coupon.getName(),
                coupon.getDescription(),
                coupon.getDiscount(),
                coupon.getMinOrderPrice(),
                coupon.getEndAt()
            );
        }
    }
}
