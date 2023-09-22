package com.prgrms.nabmart.domain.review.service;

import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewSchedulerService {

    private final RedisCacheService redisCacheService;
    private final ReviewRepository reviewRepository;

    private static final String AVERAGE_RATE_CACHE_KEY = "averageRating:Item:";
    private static final String REVIEW_COUNT_CACHE_KEY = "reviewCount:Item:";

    @Scheduled(cron = "0 0 * * 1 *")
    public void synchronizeAverageRating() {
        reviewRepository.findAll()
            .forEach(review -> redisCacheService.synchronizeAverageRating(
                review.getItem().getItemId(),
                AVERAGE_RATE_CACHE_KEY + review.getItem().getItemId()
            ));
    }

    @Scheduled(cron = "0 0 * * 1 *")
    public void synchronizeNumberOfReview() {
        reviewRepository.findAll()
            .forEach(
                review -> redisCacheService.synchronizeNumberOfReview(
                    review.getItem().getItemId(),
                    REVIEW_COUNT_CACHE_KEY + review.getItem().getItemId()
                )
            );
    }
}
