package com.prgrms.nabmart.domain.category.controller.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterMainCategoryRequest(
    @NotBlank(message = "대카테고리명은 필수 항목입니다.") String name
) {

}