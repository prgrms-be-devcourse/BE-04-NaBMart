package com.prgrms.nabmart.domain.item.service.response;

import com.prgrms.nabmart.domain.item.Item;
import java.util.List;
import java.util.stream.Collectors;

public record FindItemsResponse(List<FindItemResponse> items) {

    public static FindItemsResponse from(final List<Item> items) {
        List<FindItemResponse> findItemResponses = items.stream()
            .map(FindItemResponse::from)
            .collect(Collectors.toList());
        return new FindItemsResponse(findItemResponses);
    }

    public record FindItemResponse(Long itemId, String name, int price, int discount,
                                   int reviewCount, int like, double rate) {

        public static FindItemResponse from(final Item item) {
            return new FindItemResponse(
                item.getItemId(), item.getName(), item.getPrice(), item.getDiscount(),
                item.getReviews().size(), item.getLikeItems().size(), item.getRate()
            );
        }
    }
}
