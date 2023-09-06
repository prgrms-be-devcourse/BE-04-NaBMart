package com.prgrms.nabmart.domain.item.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.category.repository.SubCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MainCategoryRepository mainCategoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Nested
    @DisplayName("findByItemIdLessThanAndMainCategoryOrderByIdDesc 메서드 실행 시")
    class FindByItemIdLessThanAndMainCategoryOrderByIdDesc {

        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory = CategoryFixture.subCategory(mainCategory);

        @Test
        @DisplayName("대카테고리 별로 아이템이 최신순으로 페이징 조회된다.")
        public void success() {
            //Given
            List<Item> savedItems = new ArrayList<>();
            mainCategoryRepository.save(mainCategory);
            subCategoryRepository.save(subCategory);
            for (int i = 0; i < 50; i++) {
                Item item = new Item("item" + (i + 1), 0, "0", 0, 0, 0, mainCategory, subCategory);
                itemRepository.save(item);
                savedItems.add(item);
            }

            // When
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<Item> items = itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(
                30L, mainCategory, pageRequest);

            // Then
            assertThat(items.size()).isEqualTo(5);

            List<Item> expectedItems = savedItems.subList(24, 29);
            Collections.reverse(expectedItems);
            List<String> actual = items.stream()
                .map(Item::getName)
                .toList();
            List<String> expected = expectedItems.stream()
                .map(Item::getName)
                .toList();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
        }
    }
}
