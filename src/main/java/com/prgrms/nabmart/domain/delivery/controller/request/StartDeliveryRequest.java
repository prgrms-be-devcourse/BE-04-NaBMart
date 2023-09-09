package com.prgrms.nabmart.domain.delivery.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StartDeliveryRequest(
    @NotNull(message = "배달 예상 소요 시간은 필수입니다.")
    @PositiveOrZero(message = "배달 예상 소요 시간은 음수일 수 없습니다.")
    Integer deliveryEstimateMinutes) {

}
