package com.prgrms.nabmart.domain.cart.service.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public record RegisterCartCommand(
    Long userId
) {

}
