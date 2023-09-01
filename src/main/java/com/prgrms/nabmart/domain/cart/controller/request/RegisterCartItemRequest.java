package com.prgrms.nabmart.domain.cart.controller.request;

public record RegisterCartItemRequest(Long cartId, Long itemId, Integer quantity,
                                      Boolean isChecked) {

}
