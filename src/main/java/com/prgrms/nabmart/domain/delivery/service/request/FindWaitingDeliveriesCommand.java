package com.prgrms.nabmart.domain.delivery.service.request;


import org.springframework.data.domain.Pageable;

public record FindWaitingDeliveriesCommand(Pageable pageable) {

    public static FindWaitingDeliveriesCommand from(Pageable pageable) {
        return new FindWaitingDeliveriesCommand(pageable);
    }
}
