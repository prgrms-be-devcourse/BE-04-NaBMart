package com.prgrms.nabmart.domain.coupon.support;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class CouponFixture {

    private static final String NAME = "쿠폰이름";
    private static final int DISCOUNT = 10000;
    private static final String DESCRIPTION = "쿠폰설명";
    private static final int MIN_ORDER_PRICE = 1000;
    private static final LocalDate END_AT = LocalDate.parse("2023-12-31");

    public static Coupon coupon() {
        return Coupon
            .builder()
            .name(NAME)
            .discount(DISCOUNT)
            .description(DESCRIPTION)
            .minOrderPrice(MIN_ORDER_PRICE)
            .endAt(END_AT)
            .build();
    }

    public static RegisterCouponCommand registerCouponCommand() {
        return new RegisterCouponCommand(NAME, DISCOUNT, DESCRIPTION, MIN_ORDER_PRICE, END_AT);
    }
}
