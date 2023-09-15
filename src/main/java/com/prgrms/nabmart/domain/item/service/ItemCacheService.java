package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.item.service.response.ItemRedisDto;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemCacheService {

    private final RedisTemplate<String, ItemRedisDto> redisTemplate;
    private static final String NEW_PRODUCTS_KEY = "new_products";

    public void saveNewItem(final ItemRedisDto itemRedisDto) {
        redisTemplate.opsForList().rightPush(NEW_PRODUCTS_KEY, itemRedisDto);
    }

    public List<ItemRedisDto> getNewItems() {
        ListOperations<String, ItemRedisDto> items = redisTemplate.opsForList();
        Long itemCount = items.size("new_products");
        if (itemCount == null || itemCount == 0) {
            return null;
        }
        return items.range(NEW_PRODUCTS_KEY, 0, -1);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldProducts() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minus(2, ChronoUnit.WEEKS);

        ListOperations<String, ItemRedisDto> items = redisTemplate.opsForList();
        Long itemCount = items.size("new_products");

        if (itemCount == null || itemCount == 0) {
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            ItemRedisDto item = items.index("new_products", i);
            if (item != null && item.createdAt().isBefore(twoWeeksAgo)) {
                items.remove("new_products", 1, item);
                i--;
            }
        }
    }
}
