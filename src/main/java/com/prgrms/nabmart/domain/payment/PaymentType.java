package com.prgrms.nabmart.domain.payment;

public enum PaymentType {
    CARD("카드"),
    ;

    private String value;

    PaymentType(String 카드) {
        this.value = 카드;
    }

    public String getValue() {
        return value;
    }
}
