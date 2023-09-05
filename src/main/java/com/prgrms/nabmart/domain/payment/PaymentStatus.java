package com.prgrms.nabmart.domain.payment;

public enum PaymentStatus {
    PENDING("결제 대기 중"),
    SUCCESS("결제 성공"),
    CANCELED("결제 취소"),
    ;

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
