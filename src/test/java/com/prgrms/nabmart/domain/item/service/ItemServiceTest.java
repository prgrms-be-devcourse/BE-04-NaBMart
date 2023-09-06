package com.prgrms.nabmart.domain.item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.repository.MainCategoryRepository;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse.FindItemResponse;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
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
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MainCategoryRepository mainCategoryRepository;

    @InjectMocks
    private ItemService itemService;

    @Nested
    @DisplayName("findItemsByMainCategory 메서드 실행 시")
    class FindItemsByMainCategoryTests {

        MainCategory mainCategory = CategoryFixture.mainCategory();
        SubCategory subCategory1 = new SubCategory(mainCategory, "sub1");
        SubCategory subCategory2 = new SubCategory(mainCategory, "sub2");
        SubCategory subCategory3 = new SubCategory(mainCategory, "sub2");
        SubCategory subCategory4 = new SubCategory(mainCategory, "sub2");

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            List<Item> expectedItems = List.of(
                ItemFixture.item(mainCategory, subCategory1),
                ItemFixture.item(mainCategory, subCategory2),
                ItemFixture.item(mainCategory, subCategory3),
                ItemFixture.item(mainCategory, subCategory4)
            );

            when(mainCategoryRepository.findById(anyLong())).thenReturn(
                Optional.of(mainCategory));
            when(itemRepository.findByItemIdLessThanAndMainCategoryOrderByItemIdDesc(anyLong(),
                any(), any())).thenReturn(expectedItems);

            // When
            FindItemsResponse itemsResponse = itemService.findItemsByMainCategory(5L,
                1L, 2);

            // Then
            assertThat(itemsResponse.items().size()).isEqualTo(4);

            List<String> expected = expectedItems.stream()
                .map(Item::getName)
                .toList();
            List<String> actual = itemsResponse.items().stream()
                .map(FindItemResponse::name)
                .toList();
            assertThat(expected).usingRecursiveComparison()
                .isEqualTo(actual);
        }
    }
}
