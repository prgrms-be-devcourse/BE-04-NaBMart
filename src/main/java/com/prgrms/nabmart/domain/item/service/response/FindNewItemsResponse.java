package com.prgrms.nabmart.domain.item.service.response;

import java.util.List;
import java.util.stream.Collectors;

public record FindNewItemsResponse(List<FindNewItemResponse> items) {

    public static FindNewItemsResponse from(List<FindNewItemResponse> items) {
        List<FindNewItemResponse> findNewItemResponses = items.stream()
            .map(item -> FindNewItemResponse.of(item.itemId, item.name, item.price, item.discount,
                item.reviewCount))
            .collect(Collectors.toList());
        return new FindNewItemsResponse(findNewItemResponses);
    }

    public record FindNewItemResponse(Long itemId, String name, int price, int discount,
                                      Long reviewCount) {

        public static FindNewItemResponse of(Long itemId, String name, int price, int discount,
            Long reviewCount) {
            return new FindNewItemResponse(itemId, name, price, discount, reviewCount);
        }
    }
}