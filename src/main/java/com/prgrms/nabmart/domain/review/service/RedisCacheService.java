package com.prgrms.nabmart.domain.review.service;

import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {

    private final ReviewRepository reviewRepository;
    private final RedisTemplate<String, Long> numberOfReviewsRedisTemplate;
    private final ListOperations<String, String> listOperations;

    public RedisCacheService(
        ReviewRepository reviewRepository,
        RedisTemplate<String, Long> numberOfReviewsRedisTemplate,
        RedisTemplate<String, String> rateRedisTemplate
    ) {
        this.reviewRepository = reviewRepository;
        this.numberOfReviewsRedisTemplate = numberOfReviewsRedisTemplate;
        this.listOperations = rateRedisTemplate.opsForList();
    }

    public Long getTotalNumberOfReviewsByItemId(
        final Long itemId,
        final String cacheKey
    ) {
        Long cachedCount = numberOfReviewsRedisTemplate.opsForValue().get(cacheKey);

        if (cachedCount != null) {
            return cachedCount;
        }

        long dbCount = reviewRepository.countByItem_ItemId(itemId);

        numberOfReviewsRedisTemplate.opsForValue().set(cacheKey, dbCount);

        return dbCount;
    }

    public void plusOneToTotalNumberOfReviewsByItemId(
        final Long itemId,
        final String cacheKey
    ) {
        Long cachedCount = numberOfReviewsRedisTemplate.opsForValue().get(cacheKey);

        if (cachedCount != null) {
            numberOfReviewsRedisTemplate.opsForValue().set(cacheKey, cachedCount + 1);
        }

        long dbCount = reviewRepository.countByItem_ItemId(itemId);

        numberOfReviewsRedisTemplate.opsForValue().set(cacheKey, dbCount);
    }

    public void minusOneToTotalNumberOfReviewsByItemId(
        final Long itemId,
        final String cacheKey
    ) {
        Long cachedCount = numberOfReviewsRedisTemplate.opsForValue().get(cacheKey);

        if (cachedCount != null) {
            numberOfReviewsRedisTemplate.opsForValue().set(cacheKey, cachedCount - 1);
        }

        long dbCount = reviewRepository.countByItem_ItemId(itemId);

        numberOfReviewsRedisTemplate.opsForValue().set(cacheKey, dbCount);
    }

    public double getAverageRatingByItemId(
        final Long itemId,
        final String cacheKey
    ) {
        String averageRate = listOperations.index(cacheKey, 0);

        if (averageRate != null) {
            return Double.parseDouble(averageRate);
        }

        Double dbAverageRate = reviewRepository.findAverageRatingByItemId(itemId);
        Long numberOfReviews = reviewRepository.countByItem_ItemId(itemId);

        listOperations.rightPushAll(cacheKey, String.valueOf(dbAverageRate),
            String.valueOf(numberOfReviews));

        return dbAverageRate;
    }
}
