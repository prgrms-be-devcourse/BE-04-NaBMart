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
        String averageRating = listOperations.index(cacheKey, 0);

        if (averageRating != null) {
            return Double.parseDouble(averageRating);
        }

        Double dbAverageRating = reviewRepository.findAverageRatingByItemId(itemId);
        Long numberOfReviews = reviewRepository.countByItem_ItemId(itemId);

        listOperations.rightPushAll(cacheKey, String.valueOf(dbAverageRating),
            String.valueOf(numberOfReviews));

        return dbAverageRating;
    }

    public void updateAverageRatingByItemId(
        final Long itemId,
        final String cacheKey,
        final double newRating
    ) {
        String averageRating = listOperations.index(cacheKey, 0);
        String totalNumberOfReviews = listOperations.index(cacheKey, 1);

        if (averageRating != null && totalNumberOfReviews != null) {
            double totalRating =
                Double.parseDouble(averageRating) * Long.parseLong(totalNumberOfReviews);

            long updatedTotalNumberOfReviews = Long.parseLong(totalNumberOfReviews) + 1;

            double updatedAverageRating = Math.round(
                (totalRating + newRating) / updatedTotalNumberOfReviews) * 100
                / 100.0;

            listOperations.set(cacheKey, 0, String.valueOf(updatedAverageRating));
            listOperations.set(cacheKey, 1, String.valueOf(updatedTotalNumberOfReviews));
        }

        double dbAverageRating = reviewRepository.findAverageRatingByItemId(itemId);
        long dbNumberOfReviews = reviewRepository.countByItem_ItemId(itemId);

        listOperations.rightPushAll(cacheKey, String.valueOf(dbAverageRating),
            String.valueOf(dbNumberOfReviews));
    }
}
