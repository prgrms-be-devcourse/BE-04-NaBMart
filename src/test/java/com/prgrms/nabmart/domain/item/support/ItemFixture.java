package com.prgrms.nabmart.domain.item.support;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.item.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemFixture {

    private static final String NAME = "아이템이름";
    private static final int PRICE = 1000;
    private static final String DESCRIPTION = "아이템설명";
    private static final int QUANTITY = 10;
    private static final int DISCOUNT = 0;
    private static final int MAX_QUANTITY = 10;

    public static Item item(MainCategory mainCategory, SubCategory subCategory) {
        return new Item(NAME, PRICE, DESCRIPTION, QUANTITY, DISCOUNT, MAX_QUANTITY, mainCategory,
            subCategory);
    }
}