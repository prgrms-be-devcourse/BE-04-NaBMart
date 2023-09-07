package com.prgrms.nabmart.domain.coupon.service.response;

import com.prgrms.nabmart.domain.coupon.Coupon;
import java.time.LocalDate;

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
