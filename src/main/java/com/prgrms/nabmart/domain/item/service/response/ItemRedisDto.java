package com.prgrms.nabmart.domain.item.service.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.prgrms.nabmart.domain.item.Item;
import java.time.LocalDateTime;

public record ItemRedisDto(Long itemId, String name, int price, int discount,
                           @JsonSerialize(using = LocalDateTimeSerializer.class)
                           @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                           LocalDateTime createdAt
) {

    public static ItemRedisDto from(final Item item) {
        return new ItemRedisDto(
            item.getItemId(), item.getName(), item.getPrice(), item.getDiscount(),
            item.getCreatedAt()
        );
    }
}
