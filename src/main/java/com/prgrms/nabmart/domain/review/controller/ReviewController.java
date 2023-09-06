package com.prgrms.nabmart.domain.review.controller;

import com.prgrms.nabmart.domain.review.controller.request.RegisterReviewRequest;
import com.prgrms.nabmart.domain.review.exception.ReviewException;
import com.prgrms.nabmart.domain.review.service.ReviewService;
import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    
    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ErrorTemplate> handleException(
        final ReviewException reviewException) {
        log.info(reviewException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(reviewException.getMessage()));
    }
}
