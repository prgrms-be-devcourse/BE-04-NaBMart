package com.prgrms.nabmart.domain.item.service.request;

import lombok.Builder;

@Builder
public record RegisterItemCommand(String name, int price, String description,
                                  int quantity,
                                  int discount, int maxBuyQuantity, Long mainCategoryId,
                                  Long subCategoryId) {

    public static RegisterItemCommand of(
        final String name,
        final int price,
        final String description,
        final int quantity,
        final int discount,
        final int maxBuyQuantity,
        final Long mainCategoryId,
        final Long subCategoryId
    ) {
        return RegisterItemCommand.builder()
            .name(name)
            .price(price)
            .description(description)
            .quantity(quantity)
            .discount(discount)
            .maxBuyQuantity(maxBuyQuantity)
            .mainCategoryId(mainCategoryId)
            .subCategoryId(subCategoryId)
            .build();
    }
}
