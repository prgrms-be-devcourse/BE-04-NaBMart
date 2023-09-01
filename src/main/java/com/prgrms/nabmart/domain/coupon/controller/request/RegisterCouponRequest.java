package com.prgrms.nabmart.domain.coupon.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;


public record RegisterCouponRequest(
    @NotBlank(message = "쿠폰 이름은 필수 입력 항목입니다.")
    String name,

    @Positive(message = "할인 금액은 양수이어야 합니다.")
    @NotNull(message = "할인 금액은 필수 입력 항목입니다.")
    Integer discount,

    String description,

    @PositiveOrZero(message = "최소 주문 금액은 0 이상이어야 합니다.")
    @NotNull(message = "최소 주문 금액은 필수 입력 항목입니다.")
    Integer minOrderPrice,

    @NotNull(message = "쿠폰 종료일은 필수 입력 항목입니다.")
    LocalDate endAt
) {

}