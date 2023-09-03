package com.prgrms.nabmart.domain.category.controller;

import com.prgrms.nabmart.domain.category.controller.request.RegisterMainCategoryRequest;
import com.prgrms.nabmart.domain.category.exception.DuplicateCategoryNameException;
import com.prgrms.nabmart.domain.category.service.CategoryService;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    private static final String BASE_URL = "/api/v1/main-categories/";

    @PostMapping("/main-categories")
    public ResponseEntity<Void> saveMainCategory(
        @RequestBody @Valid RegisterMainCategoryRequest registerMainCategoryRequest) {

        RegisterMainCategoryCommand registerMainCategoryCommand = RegisterMainCategoryCommand.from(
            registerMainCategoryRequest);
        Long saveMainCategoryId = categoryService.saveMainCategory(registerMainCategoryCommand);
        URI location = URI.create(BASE_URL + saveMainCategoryId);
        return ResponseEntity.created(location).build();
    }

    @ExceptionHandler(DuplicateCategoryNameException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final DuplicateCategoryNameException duplicateCategoryNameException) {
        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(duplicateCategoryNameException.getMessage()));
    }
}
