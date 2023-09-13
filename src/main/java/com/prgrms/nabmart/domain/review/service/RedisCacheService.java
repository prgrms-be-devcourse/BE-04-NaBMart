package com.prgrms.nabmart.domain.review.service;

import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final ReviewRepository reviewRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    public Long getTotalReviewsByItemId(Long itemId, String cacheKey) {
        Long cachedCount = redisTemplate.opsForValue().get(cacheKey);

        if (cachedCount != null) {
            return cachedCount;
        }

        long dbCount = reviewRepository.countByItem_ItemId(itemId);

        redisTemplate.opsForValue().set(cacheKey, dbCount);

        return dbCount;
    }
}
