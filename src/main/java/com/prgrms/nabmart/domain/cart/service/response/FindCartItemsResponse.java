package com.prgrms.nabmart.domain.cart.service.response;

import java.util.List;

public record FindCartItemsResponse(List<FindCartItemResponse> findCartItemsResponse) {

    public static FindCartItemsResponse of(final List<FindCartItemResponse> findCartItemsResponse) {
        return new FindCartItemsResponse(findCartItemsResponse);
    }
}
