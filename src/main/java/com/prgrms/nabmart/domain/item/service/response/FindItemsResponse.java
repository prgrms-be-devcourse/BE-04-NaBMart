package com.prgrms.nabmart.domain.item.service.response;

import java.util.List;

public record FindItemsResponse(PageInfoResponse pageInfo, List<FindItemResponse> items) {

    public static FindItemsResponse of(final PageInfoResponse pageInfo,
        final List<FindItemResponse> items) {
        return new FindItemsResponse(pageInfo, items);
    }

    public record PageInfoResponse(int currentPage, int totalPages, Long totalItems) {

    }

    public record FindItemResponse(Long itemId, String name, int price, int discount,
                                   int reviewCount, int like, double rate) {

    }
}
