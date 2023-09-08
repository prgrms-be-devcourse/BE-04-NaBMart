package com.prgrms.nabmart.domain.review.service.response;

import java.time.LocalDateTime;
import java.util.List;

public record FindReviewsByUserResponse(
    List<FindReviewByUserResponse> reviews
) {

    public record FindReviewByUserResponse(
        Long reviewId,
        String userNickname,
        String reviewContent,
        LocalDateTime createdAt
    ) {

        public static FindReviewByUserResponse of(
            Long reviewId,
            String userNickname,
            String reviewContent,
            LocalDateTime createdAt
        ) {
            return new FindReviewByUserResponse(reviewId, userNickname, reviewContent, createdAt);
        }
    }

    public static FindReviewsByUserResponse from(
        final List<FindReviewByUserResponse> findReviewsByUserResponse
    ) {
        return new FindReviewsByUserResponse(findReviewsByUserResponse);
    }
}
