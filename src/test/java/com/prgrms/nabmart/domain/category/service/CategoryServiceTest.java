package com.prgrms.nabmart.domain.category.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.exception.DuplicateCategoryNameException;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

public class CategoryServiceTest {

    private CategoryService categoryService;
    private MainCategoryRepository mainCategoryRepository;

    @BeforeEach
    void setUp() {
        mainCategoryRepository = mock(MainCategoryRepository.class);
        categoryService = new CategoryService(mainCategoryRepository);
    }

    @Nested
    @DisplayName("saveMainCategory 메서드 실행 시")
    class SaveMainCategoryTests {

        @Test
        @DisplayName("성공")
        @Transactional
        public void success() {
            // Given
            RegisterMainCategoryCommand command = new RegisterMainCategoryCommand("TestCategory");
            MainCategory savedMainCategory = new MainCategory("TestCategory");
            when(mainCategoryRepository.save(any(MainCategory.class))).thenReturn(
                savedMainCategory);

            // When
            categoryService.saveMainCategory(command);

            // Then
            verify(mainCategoryRepository, times(1)).save(any(MainCategory.class));
        }

        @Test
        @DisplayName("예외: 이미 존재하는 MainCategory 이름")
        public void throwExceptionWhenNameIsDuplicated() {
            // Given
            RegisterMainCategoryCommand command = new RegisterMainCategoryCommand("TestCategory");
            when(mainCategoryRepository.existsByName("TestCategory")).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> categoryService.saveMainCategory(command))
                .isInstanceOf(DuplicateCategoryNameException.class);
            verify(mainCategoryRepository, times(1)).existsByName("TestCategory");
        }
    }
}
