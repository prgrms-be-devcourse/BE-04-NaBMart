package com.prgrms.nabmart.domain.cart.service.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public record RegisterCartCommand(
    @NotNull(message = "구매자 아이디는 필수 입력 항목입니다.") @Positive(message = "구매자 아이디는 양수입니다.") Long userId
) {

}
