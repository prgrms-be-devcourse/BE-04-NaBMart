package com.prgrms.nabmart.domain.item.service.response;

public record FindItemResponse(Long itemId, String name, int price, int discount, int reviewCount,
                               int like, int rate) {

    public static FindItemResponse of(final Long itemId, final String name, final int price,
        final int discount, final int reviewCount, final int like, final int rate) {
        return new FindItemResponse(itemId, name, price, discount, reviewCount, like, rate);
    }
}
