package com.prgrms.nabmart.domain.delivery.service.request;

import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;

public record FindRiderDeliveriesCommand(
    Long riderId,
    List<DeliveryStatus> deliveryStatuses,
    Pageable pageable) {

    public static FindRiderDeliveriesCommand of(
        final Long riderId,
        final List<DeliveryStatus> deliveryStatuses,
        final Pageable pageable) {
        return new FindRiderDeliveriesCommand(riderId, deliveryStatuses, pageable);
    }
}
