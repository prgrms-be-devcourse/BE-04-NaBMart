package com.prgrms.nabmart.global.fixture;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.controller.request.RegisterMainCategoryRequest;
import com.prgrms.nabmart.domain.category.controller.request.RegisterSubCategoryRequest;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import com.prgrms.nabmart.domain.category.service.request.RegisterSubCategoryCommand;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryFixture {

    private static final String MAIN_CATEGORY_NAME = "대카테고리";
    private static final String SUB_CATEGORY_NAME = "소카테고리";
    private static final Long MAIN_CATEGORY_ID = 1L;

    public static MainCategory mainCategory() {
        return new MainCategory(MAIN_CATEGORY_NAME);
    }

    public static SubCategory subCategory(MainCategory mainCategory) {
        return new SubCategory(mainCategory, SUB_CATEGORY_NAME);
    }

    public static RegisterSubCategoryRequest registerSubCategoryRequest() {
        return new RegisterSubCategoryRequest(MAIN_CATEGORY_ID, SUB_CATEGORY_NAME);
    }

    public static RegisterSubCategoryCommand registerSubCategoryCommand() {
        return new RegisterSubCategoryCommand(MAIN_CATEGORY_ID, SUB_CATEGORY_NAME);
    }

    public static RegisterMainCategoryRequest registerMainCategoryRequest() {
        return new RegisterMainCategoryRequest(MAIN_CATEGORY_NAME);
    }

    public static RegisterMainCategoryCommand registerMainCategoryCommand() {
        return new RegisterMainCategoryCommand(MAIN_CATEGORY_NAME);
    }

    public static List<MainCategory> mainCategories() {
        MainCategory mainCategory1 = new MainCategory("main1");
        MainCategory mainCategory2 = new MainCategory("main2");
        MainCategory mainCategory3 = new MainCategory("main3");
        return List.of(mainCategory1, mainCategory2, mainCategory3);
    }

    public static List<String> mainCategoryNames() {
        return List.of("main1", "main2", "main3");
    }
}
