package com.prgrms.nabmart.domain.item.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateItemRequest(

    @NotBlank(message = "상품명은 필수 항목입니다.")
    String name,

    @PositiveOrZero(message = "상품 가격은 0원 이상이어야 합니다.")
    int price,

    @PositiveOrZero(message = "상품 수량은 0개 이상이어야 합니다.")
    int quantity,

    @Min(value = 0, message = "상품 할인율은 0% 이상이어야 합니다.")
    @Max(value = 100, message = "상품 할인율은 100% 이하어야 합니다.")
    int discount,

    String description,
    Long mainCategoryId,
    Long subCategoryId
) {

    public static UpdateItemRequest of(
        final String name,
        final int price,
        final int quantity,
        final String description,
        final Long mainCategoryId,
        final Long subCategoryId,
        final int discount
    ) {
        return new UpdateItemRequest(name, price, quantity, discount, description,
            mainCategoryId, subCategoryId);
    }
}
