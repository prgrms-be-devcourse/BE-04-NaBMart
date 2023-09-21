package com.prgrms.nabmart.domain.payment.service.request;

public record FindPayedOrdersCommand(Long userId, int page) {

    public static FindPayedOrdersCommand of(final Long userId, final int page) {
        return new FindPayedOrdersCommand(userId, page);
    }
}
