package com.prgrms.nabmart.domain.order.service.request;

public record UpdateOrderByCouponCommand(
    Long orderId,
    Long userId,
    Long couponId
) {

    public static UpdateOrderByCouponCommand of(
        final Long orderId,
        final Long userId,
        final Long couponId
    ) {
        return new UpdateOrderByCouponCommand(orderId, userId, couponId);
    }

}
