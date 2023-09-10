package com.prgrms.nabmart.domain.delivery.service.request;


import org.springframework.data.domain.Pageable;

public record FindDeliveriesCommand(Pageable pageable) {

    public static FindDeliveriesCommand from(Pageable pageable) {
        return new FindDeliveriesCommand(pageable);
    }
}
