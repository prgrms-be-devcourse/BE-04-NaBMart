package com.prgrms.nabmart.domain.item.service.response;

import com.prgrms.nabmart.domain.item.Item;
import java.time.LocalDateTime;

public record ItemRedisDto(Long itemId, String name, int price, int discount,
                           LocalDateTime createdAt) {

    public static ItemRedisDto from(final Item item) {
        return new ItemRedisDto(
            item.getItemId(), item.getName(), item.getPrice(), item.getDiscount(),
            item.getCreatedAt()
        );
    }
}
