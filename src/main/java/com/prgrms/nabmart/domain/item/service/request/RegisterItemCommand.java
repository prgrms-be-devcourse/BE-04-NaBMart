package com.prgrms.nabmart.domain.item.service.request;

import com.prgrms.nabmart.domain.item.controller.request.RegisterItemRequest;
import lombok.Builder;

@Builder
public record RegisterItemCommand(String name, int price, String description,
                                  int quantity,
                                  int discount, int maxBuyQuantity, Long mainCategoryId,
                                  Long subCategoryId) {

    public static RegisterItemCommand from(RegisterItemRequest registerItemRequest) {
        return RegisterItemCommand.builder()
            .name(registerItemRequest.name())
            .price(registerItemRequest.price())
            .description(registerItemRequest.description())
            .quantity(registerItemRequest.quantity())
            .discount(registerItemRequest.discount())
            .maxBuyQuantity(registerItemRequest.maxBuyQuantity())
            .mainCategoryId(registerItemRequest.mainCategoryId())
            .subCategoryId(registerItemRequest.subCategoryId())
            .build();
    }
}
