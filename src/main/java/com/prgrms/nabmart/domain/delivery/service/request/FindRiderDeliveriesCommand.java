package com.prgrms.nabmart.domain.delivery.service.request;

import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import org.springframework.data.domain.Pageable;

public record FindRiderDeliveriesCommand(
    Long riderId,
    DeliveryStatus deliveryStatus,
    Pageable pageable) {

    public static FindRiderDeliveriesCommand of(
        Long riderId,
        DeliveryStatus deliveryStatus,
        Pageable pageable) {
        return new FindRiderDeliveriesCommand(riderId, deliveryStatus, pageable);
    }
}
