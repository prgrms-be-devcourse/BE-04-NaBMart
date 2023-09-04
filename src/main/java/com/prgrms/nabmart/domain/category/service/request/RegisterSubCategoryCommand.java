package com.prgrms.nabmart.domain.category.service.request;

import com.prgrms.nabmart.domain.category.controller.request.RegisterSubCategoryRequest;
import jakarta.validation.constraints.NotBlank;

public record RegisterSubCategoryCommand(
    @NotBlank(message = "소카테고리명은 필수 항목입니다.") String name
) {

    public static RegisterSubCategoryCommand from(
        RegisterSubCategoryRequest registerSubCategoryRequest) {
        return new RegisterSubCategoryCommand(registerSubCategoryRequest.name());
    }
}
