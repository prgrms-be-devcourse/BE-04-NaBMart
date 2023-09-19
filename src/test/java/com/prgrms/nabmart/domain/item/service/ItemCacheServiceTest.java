package com.prgrms.nabmart.domain.item.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.prgrms.nabmart.base.RedisTestContainerConfig;
import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.service.response.ItemRedisDto;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
class ItemCacheServiceTest extends RedisTestContainerConfig {

    @Autowired
    private ItemCacheService itemCacheService;

    @Autowired
    private RedisTemplate<String, ItemRedisDto> redisTemplate;

    private static final String NEW_PRODUCTS_KEY = "new_products";
    Item item;
    MainCategory mainCategory;
    SubCategory subCategory;

    @BeforeEach
    void setUp() {
        mainCategory = CategoryFixture.mainCategory();
        subCategory = CategoryFixture.subCategory(mainCategory);
        item = ItemFixture.item(mainCategory, subCategory);
    }

    @AfterEach
    public void cleanupRedis() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Nested
    @DisplayName("새롭게 등록된 상품을 Redis에도 등록한다.")
    class SaveItemRedisTest {

        @Test
        @DisplayName("성공")
        void saveNewItem() {
            // Given
            ItemRedisDto itemRedisDto = ItemRedisDto.from(item);

            // When
            itemCacheService.saveNewItem(itemRedisDto);

            // Then
            assertThat(redisTemplate.opsForList().size(NEW_PRODUCTS_KEY)).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Redis에서 신상품을 조회한다.")
    class getNewItemsTest {

        @Test
        @DisplayName("성공")
        void getNewItems() {
            // Given
            ItemRedisDto itemRedisDto = ItemRedisDto.from(item);
            List<ItemRedisDto> expectedItems = List.of(itemRedisDto);

            itemCacheService.saveNewItem(itemRedisDto);

            // When
            List<ItemRedisDto> result = itemCacheService.getNewItems();

            // Then
            assertThat(result).isEqualTo(expectedItems);
        }
    }
}