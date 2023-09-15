package com.prgrms.nabmart.domain.item.service;

import com.prgrms.nabmart.domain.item.ItemSortType;
import com.prgrms.nabmart.domain.item.service.response.ItemRedisDto;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<ItemRedisDto> getNewItems(ItemSortType sortType) {
        return getNewItemsSorted(sortType);
    }

    private List<ItemRedisDto> getNewItemsSorted(ItemSortType sortType) {
        ListOperations<String, ItemRedisDto> listOperations = redisTemplate.opsForList();
        Long itemCount = listOperations.size(NEW_PRODUCTS_KEY);
        if (itemCount == null || itemCount == 0) {
            return null;
        }

        List<ItemRedisDto> items = listOperations.range(NEW_PRODUCTS_KEY, 0, -1);

        return switch (sortType) {
            case LOWEST_AMOUNT -> items.stream()
                .sorted(Comparator.comparingInt(ItemRedisDto::price))
                .collect(Collectors.toList());
            case HIGHEST_AMOUNT -> items.stream()
                .sorted(Comparator.comparingInt(ItemRedisDto::price).reversed())
                .collect(Collectors.toList());
            case DISCOUNT -> items.stream()
                .sorted(Comparator.comparingInt(ItemRedisDto::discount).reversed())
                .collect(Collectors.toList());
            default -> items.stream()
                .sorted(Comparator.comparingLong(ItemRedisDto::itemId).reversed())
                .collect(Collectors.toList());
        };
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldProducts() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minus(2, ChronoUnit.WEEKS);

        ListOperations<String, ItemRedisDto> items = redisTemplate.opsForList();
        Long itemCount = items.size(NEW_PRODUCTS_KEY);

        if (itemCount == null || itemCount == 0) {
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            ItemRedisDto item = items.index(NEW_PRODUCTS_KEY, i);
            if (item != null && item.createdAt().isBefore(twoWeeksAgo)) {
                items.remove(NEW_PRODUCTS_KEY, 1, item);
                i--;
            }
        }
    }
}
