package com.prgrms.nabmart.domain.cart.service.response;

import java.util.List;

public record FindCartItemsResponse(List<FindCartItemResponse> findCartItemsResponse) {

    public static FindCartItemsResponse from(
        final List<FindCartItemResponse> findCartItemsResponse) {
        return new FindCartItemsResponse(findCartItemsResponse);
    }
}
