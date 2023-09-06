package com.prgrms.nabmart.domain.review.controller;

import com.prgrms.nabmart.domain.review.controller.request.RegisterReviewRequest;
import com.prgrms.nabmart.domain.review.controller.request.UpdateReviewRequest;
import com.prgrms.nabmart.domain.review.exception.ReviewException;
import com.prgrms.nabmart.domain.review.service.ReviewService;
import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import com.prgrms.nabmart.domain.review.service.request.UpdateReviewCommand;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private static final String BASE_URL = "/api/v1/reviews/";

    @PostMapping
    public ResponseEntity<Void> registerReview(
        @Valid @RequestBody RegisterReviewRequest registerReviewRequest,
        @LoginUser Long userId
    ) {
        RegisterReviewCommand registerReviewCommand = RegisterReviewCommand.of(
            userId, registerReviewRequest.itemId(), registerReviewRequest.rate(),
            registerReviewRequest.content()
        );

        Long reviewId = reviewService.registerReview(registerReviewCommand);

        URI location = URI.create(BASE_URL + reviewId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
        @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
        @PathVariable final Long reviewId,
        @Valid @RequestBody UpdateReviewRequest updateReviewRequest
    ) {
        UpdateReviewCommand updateReviewCommand = UpdateReviewCommand.of(
            reviewId,
            updateReviewRequest.rate(),
            updateReviewRequest.content()
        );

        reviewService.updateReview(updateReviewCommand);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final ReviewException reviewException) {
        log.info(reviewException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(reviewException.getMessage()));
    }
}
