package com.prgrms.nabmart.domain.item.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.domain.ItemSortType;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.global.fixture.ItemFixture;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class ItemServiceTest {

    private ItemService itemService;

    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemService(itemRepository);
    }

    @Nested
    @DisplayName("findNewItems 메서드 실행 시")
    class FindNewItemsTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            FindNewItemsCommand command = FindNewItemsCommand.of(0, 3, ItemSortType.LOWEST_AMOUNT);
            PageRequest pageRequest = PageRequest.of(command.page(), command.pageSize());
            LocalDateTime createdAt = LocalDateTime.now().minus(2, ChronoUnit.WEEKS);

            List<Item> itemList = Arrays.asList(
                ItemFixture.item(CategoryFixture.mainCategory(), CategoryFixture.subCategory(CategoryFixture.mainCategory())),
                ItemFixture.item(CategoryFixture.mainCategory(), CategoryFixture.subCategory(CategoryFixture.mainCategory()))
            );

            Page<Item> mockItems = new PageImpl<>(itemList, pageRequest, 20);

            when(itemRepository.findByCreatedAtAfter(createdAt, pageRequest)).thenReturn(mockItems);

            // When
            itemService.findNewItems(command);

            // Then
            verify(itemRepository, times(1)).findByCreatedAtAfter(createdAt, pageRequest);
        }
    }

}