package com.prgrms.nabmart.domain.delivery.controller.request;

import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record FindRiderDeliveriesRequest(
    @NotNull(message = "배달 상태는 필수입니다.")
    DeliveryStatus deliveryStatus) {

}
