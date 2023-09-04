package com.prgrms.nabmart.domain.category.service.request;

import com.prgrms.nabmart.domain.category.controller.request.RegisterSubCategoryRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterSubCategoryCommand(
    @NotNull(message = "대카테고리는 필수 항목입니다.") @Positive(message = "없는 대카테고리 Id입니다.") Long mainCategoryId,
    @NotBlank(message = "소카테고리명은 필수 항목입니다.") String name
) {

    public static RegisterSubCategoryCommand from(
        RegisterSubCategoryRequest registerSubCategoryRequest
    ) {
        return new RegisterSubCategoryCommand(
            registerSubCategoryRequest.mainCategoryId(),
            registerSubCategoryRequest.name()
        );
    }
}
