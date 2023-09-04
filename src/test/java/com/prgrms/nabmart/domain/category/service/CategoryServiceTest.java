package com.prgrms.nabmart.domain.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.exception.DuplicateCategoryNameException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import com.prgrms.nabmart.domain.category.service.request.RegisterSubCategoryCommand;
import com.prgrms.nabmart.domain.category.service.response.FindMainCategoriesResponse;
import com.prgrms.nabmart.global.fixture.CategoryFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private MainCategoryRepository mainCategoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Nested
    @DisplayName("saveMainCategory 메서드 실행 시")
    class SaveMainCategoryTests {

        RegisterMainCategoryCommand registerMainCategoryCommand = CategoryFixture.registerMainCategoryCommand();
        MainCategory mainCategory = CategoryFixture.mainCategory();

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            when(mainCategoryRepository.save(any(MainCategory.class))).thenReturn(
                mainCategory);

            // When
            categoryService.saveMainCategory(registerMainCategoryCommand);

            // Then
            verify(mainCategoryRepository, times(1)).save(any(MainCategory.class));
        }

        @Test
        @DisplayName("예외: 이미 존재하는 MainCategory 이름")
        public void throwExceptionWhenNameIsDuplicated() {
            // Given
            when(
                mainCategoryRepository.existsByName(registerMainCategoryCommand.name())).thenReturn(
                true);

            // When & Then
            assertThatThrownBy(() -> categoryService.saveMainCategory(registerMainCategoryCommand))
                .isInstanceOf(DuplicateCategoryNameException.class);
            verify(mainCategoryRepository, times(1)).existsByName(
                registerMainCategoryCommand.name());
        }
    }


    @Nested
    @DisplayName("saveSubCategory 메서드 실행 시")
    class SaveSubCategoryTests {

        RegisterSubCategoryCommand registerSubCategoryCommand = CategoryFixture.registerSubCategoryCommand();
        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = CategoryFixture.subCategory(mainCategory);

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            when(mainCategoryRepository.findById(anyLong())).thenReturn(Optional.of(mainCategory));
            when(subCategoryRepository.existsByMainCategoryAndName(any(), any())).thenReturn(
                false);
            when(subCategoryRepository.save(any())).thenReturn(subCategory);

            // When
            categoryService.saveSubCategory(registerSubCategoryCommand);

            // Then

            verify(subCategoryRepository, times(1)).save(any(SubCategory.class));
            verify(mainCategoryRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("예외: MainCategory 내 이미 존재하는 SubCategory 이름")
        public void throwExceptionWhenNameIsDuplicated() {
            // Given
            when(mainCategoryRepository.findById(anyLong())).thenReturn(Optional.of(mainCategory));
            when(subCategoryRepository.existsByMainCategoryAndName(any(), any())).thenReturn(
                true);

            // When & Then
            assertThatThrownBy(() -> categoryService.saveSubCategory(registerSubCategoryCommand))
                .isInstanceOf(DuplicateCategoryNameException.class);
            verify(subCategoryRepository, times(1)).existsByMainCategoryAndName(any(), any());
            verify(mainCategoryRepository, times(1)).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("모든 대카테고리 조회 메서드 호출 시")
    class FindAllMainCategories {

        List<String> mainCategoryNames = CategoryFixture.mainCategoryNames();
        List<MainCategory> mainCategories = CategoryFixture.mainCategories();

        @Test
        @DisplayName("성공")
        public void success() {

            // Then
            when(mainCategoryRepository.findAll()).thenReturn(mainCategories);

            // When
            FindMainCategoriesResponse findMainCategoriesResponse = categoryService.findAllMainCategories();

            // Then
            assertThat(findMainCategoriesResponse.mainCategoryNames())
                .usingRecursiveComparison()
                .isEqualTo(mainCategoryNames);
            verify(mainCategoryRepository, times(1)).findAll();
        }
    }
}
