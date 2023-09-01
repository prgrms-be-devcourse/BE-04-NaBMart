package com.prgrms.nabmart.domain.category.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.nabmart.domain.category.MainCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class MainCategoryRepositoryTest {

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Nested
    @DisplayName("save 메서드")
    class SaveMethod {

        @Test
        @DisplayName("성공")
        public void saveMainCategory_Success() {
            // Given
            String categoryName = "test category";
            MainCategory mainCategory = new MainCategory(categoryName);

            // When
            MainCategory savedMainCategory = mainCategoryRepository.save(mainCategory);

            // Then
            assertThat(savedMainCategory.getName()).isEqualTo(categoryName);
        }

        @Test
        @DisplayName("예외: 이름이 null")
        public void saveMainCategory_WithNullName_ShouldThrowException() {
            // Given
            MainCategory mainCategory = new MainCategory(null);

            // When & Then
            assertThatThrownBy(() -> mainCategoryRepository.save(mainCategory))
                .isInstanceOf(DataIntegrityViolationException.class);
        }
    }
}
