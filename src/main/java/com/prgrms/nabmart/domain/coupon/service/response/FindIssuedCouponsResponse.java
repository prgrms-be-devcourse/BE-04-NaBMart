package com.prgrms.nabmart.domain.coupon.service.response;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.UserCoupon;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record FindIssuedCouponsResponse(List<FindIssuedCouponResponse> coupons) {

    public static FindIssuedCouponsResponse from(final List<UserCoupon> IssuedCoupons) {
        return IssuedCoupons.stream()
            .map(UserCoupon::getCoupon)
            .map(FindIssuedCouponResponse::from)
            .collect(
                Collectors.collectingAndThen(Collectors.toList(), FindIssuedCouponsResponse::new));
    }

    public record FindIssuedCouponResponse(
        Long couponId,
        String name,
        String description,
        Integer discount,
        Integer minOrderPrice,
        LocalDate endAt
    ) {

        public static FindIssuedCouponResponse from(final Coupon coupon) {
            return new FindIssuedCouponResponse(
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
