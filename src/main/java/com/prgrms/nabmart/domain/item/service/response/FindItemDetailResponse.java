package com.prgrms.nabmart.domain.item.service.response;

public record FindItemDetailResponse(Long itemId, String name, int price, String description,
                                     int quantity, double rate, int reviewCount, int discount,
                                     int like, int maxBuyQuantity) {

    public static FindItemDetailResponse of(final Long itemId, final String name, final int price,
        final String description,
        final int quantity,
        final double rate, final int reviewCount, final int discount, final int like,
        final int maxBuyQuantity) {
        return new FindItemDetailResponse(itemId, name, price, description, quantity, rate,
            reviewCount, discount, like, maxBuyQuantity);
    }
}
