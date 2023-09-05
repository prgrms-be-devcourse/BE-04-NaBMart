package com.prgrms.nabmart.domain.category.controller;

import com.prgrms.nabmart.domain.category.controller.request.RegisterMainCategoryRequest;
import com.prgrms.nabmart.domain.category.controller.request.RegisterSubCategoryRequest;
import com.prgrms.nabmart.domain.category.exception.DuplicateCategoryNameException;
import com.prgrms.nabmart.domain.category.service.CategoryService;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import com.prgrms.nabmart.domain.category.service.request.RegisterSubCategoryCommand;
import com.prgrms.nabmart.domain.category.service.response.FindMainCategoriesResponse;
import com.prgrms.nabmart.domain.category.service.response.FindSubCategoriesResponse;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;
    private static final String MAIN_CATEGORY_BASE_URL = "/api/v1/main-categories/";
    private static final String SUB_CATEGORY_BASE_URL = "/api/v1/sub-categories/";

    @PostMapping("/main-categories")
    public ResponseEntity<Void> saveMainCategory(
        @RequestBody @Valid RegisterMainCategoryRequest registerMainCategoryRequest) {

        RegisterMainCategoryCommand registerMainCategoryCommand = RegisterMainCategoryCommand.from(
            registerMainCategoryRequest);
        Long savedMainCategoryId = categoryService.saveMainCategory(registerMainCategoryCommand);
        URI location = URI.create(MAIN_CATEGORY_BASE_URL + savedMainCategoryId);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/sub-categories")
    public ResponseEntity<Void> saveSubCategory(
        @RequestBody @Valid RegisterSubCategoryRequest registerSubCategoryRequest) {

        RegisterSubCategoryCommand registerSubCategoryCommand = RegisterSubCategoryCommand.from(
            registerSubCategoryRequest);
        Long savedSubCategoryId = categoryService.saveSubCategory(registerSubCategoryCommand);
        URI location = URI.create(SUB_CATEGORY_BASE_URL + savedSubCategoryId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/categories")
    public ResponseEntity<FindMainCategoriesResponse> findAllMainCategories() {
        FindMainCategoriesResponse findMainCategoriesResponse = categoryService.findAllMainCategories();
        return ResponseEntity.ok(findMainCategoriesResponse);
    }

    @GetMapping("/categories/{mainCategoryId}")
    public ResponseEntity<FindSubCategoriesResponse> findSubCategories(
        @PathVariable Long mainCategoryId) {

        FindSubCategoriesResponse findSubCategoriesResponse = categoryService.findSubCategoriesByMainCategory(
            mainCategoryId);
        return ResponseEntity.ok(findSubCategoriesResponse);
    }

    @ExceptionHandler(DuplicateCategoryNameException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final DuplicateCategoryNameException duplicateCategoryNameException) {
        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(duplicateCategoryNameException.getMessage()));
    }
}
