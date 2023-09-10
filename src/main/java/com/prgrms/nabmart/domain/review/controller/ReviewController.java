package com.prgrms.nabmart.domain.review.controller;

import com.prgrms.nabmart.domain.review.controller.request.RegisterReviewRequest;
import com.prgrms.nabmart.domain.review.controller.request.UpdateReviewRequest;
import com.prgrms.nabmart.domain.review.exception.ReviewException;
import com.prgrms.nabmart.domain.review.service.ReviewService;
import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import com.prgrms.nabmart.domain.review.service.request.UpdateReviewCommand;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByItemResponse;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByUserResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    private static final String BASE_URI = "/api/v1/reviews/";

    @PostMapping("/reviews")
    public ResponseEntity<Void> registerReview(
        @Valid @RequestBody RegisterReviewRequest registerReviewRequest,
        @LoginUser Long userId
    ) {
        RegisterReviewCommand registerReviewCommand = RegisterReviewCommand.of(
            userId, registerReviewRequest.itemId(), registerReviewRequest.rate(),
            registerReviewRequest.content()
        );

        Long reviewId = reviewService.registerReview(registerReviewCommand);

        URI location = URI.create(BASE_URI + reviewId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
        @PathVariable final Long reviewId
    ) {
        reviewService.deleteReview(reviewId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reviews/{reviewId}")
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

    @GetMapping("/users/my-reviews")
    public ResponseEntity<FindReviewsByUserResponse> findReviewsByUser(
        @LoginUser final Long userId
    ) {
        return ResponseEntity.ok().body(reviewService.findReviewsByUser(userId));
    }

    @GetMapping("/items/{itemId}/reviews")
    public ResponseEntity<FindReviewsByItemResponse> findReviewsByItem(
        @PathVariable final Long itemId
    ) {
        return ResponseEntity.ok()
            .body(reviewService.findReviewsByItem(itemId));
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final ReviewException reviewException) {
        log.info(reviewException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(reviewException.getMessage()));
    }
}
