package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.item.controller.request.UpdateItemRequest;
import lombok.Builder;

@Builder
public record UpdateItemCommand(Long itemId, String name, int quantity, int price,
                                String description, Long mainCategoryId, Long subCategoryId,
                                int discount) {

    public static UpdateItemCommand of(
        final Long itemId,
        final String name,
        final int quantity,
        final int price,
        final String description,
        final Long mainCategoryId,
        final Long subCategoryId,
        final int discount
    ) {
        return new UpdateItemCommand(itemId, name, quantity, price, description, mainCategoryId,
            subCategoryId, discount);
    }

    public static UpdateItemCommand of(final Long itemId,
        final UpdateItemRequest updateItemRequest) {
        return UpdateItemCommand.builder()
            .itemId(itemId)
            .name(updateItemRequest.name())
            .quantity(updateItemRequest.quantity())
            .price(updateItemRequest.price())
            .description(updateItemRequest.description())
            .mainCategoryId(updateItemRequest.mainCategoryId())
            .subCategoryId(updateItemRequest.subCategoryId())
            .discount(updateItemRequest.discount())
            .build();
    }
}
