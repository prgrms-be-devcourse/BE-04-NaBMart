package com.prgrms.nabmart.domain.order.service.response;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.order.Order;

public record UpdateOrderByCouponResponse(
    Integer totalPrice,
    Integer discountPrice
) {

    public static UpdateOrderByCouponResponse of(final Order order, final Coupon coupon) {
        return new UpdateOrderByCouponResponse(order.getPrice(), coupon.getDiscount());
    }
}
