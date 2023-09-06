package com.prgrms.nabmart.domain.item.service.response;

public record FindItemDetailResponse(Long itemId, String name, int price, String description,
                                     int quantity, double rate, int reviewCount, int discount,
                                     int like, int maxBuyQuantity) {

    public static FindItemDetailResponse of(Long itemId, String name, int price, String description,
        int quantity,
        double rate, int reviewCount, int discount, int like, int maxBuyQuantity) {
        return new FindItemDetailResponse(itemId, name, price, description, quantity, rate,
            reviewCount, discount, like, maxBuyQuantity);
    }
}
