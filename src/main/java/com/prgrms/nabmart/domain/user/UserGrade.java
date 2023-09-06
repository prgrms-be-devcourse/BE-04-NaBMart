package com.prgrms.nabmart.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGrade {
    NONE(-1),
    NORMAL(0),
    VIP(5),
    VVIP(10),
    RVIP(20);

    private final int minimumOrderCount;

    public static UserGrade calculateUserGrade(int orderCount) {
        if (orderCount >= 20) {
            return RVIP;
        } else if(orderCount >= 10) {
            return VVIP;
        } else if(orderCount >= 5) {
            return VIP;
        } else {
            return NORMAL;
        }
    }
}
