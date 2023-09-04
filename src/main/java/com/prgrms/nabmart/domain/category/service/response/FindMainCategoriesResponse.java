package com.prgrms.nabmart.domain.category.service.response;

import com.prgrms.nabmart.domain.category.MainCategory;
import java.util.List;
import java.util.stream.Collectors;

public record FindMainCategoriesResponse(List<String> mainCategoryNames) {

    public static FindMainCategoriesResponse from(List<MainCategory> mainCategories) {
        List<String> mainCategoryNames = mainCategories.stream()
            .map(MainCategory::getName)
            .collect(Collectors.toList());
        return new FindMainCategoriesResponse(mainCategoryNames);
    }
}
