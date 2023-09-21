package com.prgrms.nabmart.domain.delivery.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterDeliveryRequest(
    @Positive(message = "배달 예상 소요 시간은 양수여야 합니다.")
    @NotNull(message = "배달 예상 소요 시간은 필수입니다.")
    Integer estimateMinutes) {

}
