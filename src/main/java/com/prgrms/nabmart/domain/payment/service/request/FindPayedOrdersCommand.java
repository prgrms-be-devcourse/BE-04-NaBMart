package com.prgrms.nabmart.domain.payment.service.request;

public record FindPayedOrdersCommand(Long userId, int page) {

    public static FindPayedOrdersCommand of(Long userId, int page) {
        return new FindPayedOrdersCommand(userId, page);
    }
}
