package com.prgrms.nabmart.domain.item.service.response;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.LikeItem;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindLikeItemsResponse(
    List<FindLikeItemResponse> items,
    int page,
    long totalElements) {

    public static FindLikeItemsResponse from(final Page<LikeItem> likeItemPage) {
        Page<FindLikeItemResponse> findLikeItemResponsePage
            = likeItemPage.map(FindLikeItemResponse::from);
        return new FindLikeItemsResponse(
            findLikeItemResponsePage.getContent(),
            findLikeItemResponsePage.getNumber(),
            findLikeItemResponsePage.getTotalElements());
    }

    public record FindLikeItemResponse(
        Long likeItemId,
        Long itemId,
        String name,
        int price,
        int discount,
        int reviewCount,
        int like,
        double rate) {

        public static FindLikeItemResponse from(LikeItem likeItem) {
            Item item = likeItem.getItem();
            return new FindLikeItemResponse(
                likeItem.getLikeItemId(),
                item.getItemId(),
                item.getName(),
                item.getPrice(),
                item.getDiscount(),
                item.getReviews().size(),
                item.getLikeItems().size(),
                item.getRate());
        }
    }
}
