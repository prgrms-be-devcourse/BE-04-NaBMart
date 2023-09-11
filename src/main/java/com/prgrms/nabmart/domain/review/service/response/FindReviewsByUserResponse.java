package com.prgrms.nabmart.domain.review.service.response;

import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.user.User;
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
            final Long reviewId,
            final String userNickname,
            final String reviewContent,
            final LocalDateTime createdAt
        ) {
            return new FindReviewByUserResponse(reviewId, userNickname, reviewContent, createdAt);
        }
    }

    public static FindReviewsByUserResponse of(
        final User user,
        final List<Review> reviews
    ) {
        List<FindReviewByUserResponse> findReviewByUserResponses = reviews
            .stream()
            .map(
                review -> FindReviewByUserResponse.of(
                    review.getReviewId(),
                    user.getNickname(),
                    review.getContent(),
                    review.getCreatedAt()
                )
            ).toList();

        return new FindReviewsByUserResponse(findReviewByUserResponses);
    }
}
