package com.prgrms.nabmart.domain.cart.service.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public record RegisterCartCommand(Long userId) {

    public static RegisterCartCommand of(Long userId) {
        return new RegisterCartCommand(userId);
    }
}
