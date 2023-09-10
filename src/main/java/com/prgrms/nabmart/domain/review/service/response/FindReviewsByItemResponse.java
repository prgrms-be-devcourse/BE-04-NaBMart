package com.prgrms.nabmart.domain.review.service.response;

import com.prgrms.nabmart.domain.review.Review;
import java.time.LocalDateTime;
import java.util.List;

public record FindReviewsByItemResponse(
    List<FindReviewByItemResponse> reviews
) {

    public record FindReviewByItemResponse(
        Long reviewId,
        Long itemId,
        String reviewContent,
        LocalDateTime createdAt
    ) {

        public static FindReviewByItemResponse of(
            final Long reviewId,
            final Long itemId,
            final String reviewContent,
            final LocalDateTime createdAt
        ) {
            return new FindReviewByItemResponse(reviewId, itemId, reviewContent, createdAt);
        }

    }

    public static FindReviewsByItemResponse of(
        final Long itemId,
        final List<Review> reviews
    ) {
        List<FindReviewByItemResponse> findReviewByItemResponses = reviews
            .stream()
            .map(
                review -> FindReviewByItemResponse.of(
                    review.getReviewId(),
                    itemId,
                    review.getContent(),
                    review.getCreatedAt()
                )
            ).toList();

        return new FindReviewsByItemResponse(findReviewByItemResponses);
    }
}
