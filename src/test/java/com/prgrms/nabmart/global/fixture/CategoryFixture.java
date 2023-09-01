package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.category.domain.MainCategory;
import com.prgrms.nabmart.domain.category.domain.SubCategory;

public final class CategoryFixture {

    private static final String MAIN_CATEGORY_NAME = "대카테고리";
    private static final String SUB_CATEGORY_NAME = "대카테고리";

    private CategoryFixture() {
    }

    public static MainCategory mainCategory() {
        return new MainCategory(MAIN_CATEGORY_NAME);
    }

    public static SubCategory subCategory(MainCategory mainCategory) {
        return new SubCategory(mainCategory, SUB_CATEGORY_NAME);
    }
}
