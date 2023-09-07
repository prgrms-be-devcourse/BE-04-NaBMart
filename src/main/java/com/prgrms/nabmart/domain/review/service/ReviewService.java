package com.prgrms.nabmart.domain.review.service;


import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.review.exception.NotFoundReviewException;
import com.prgrms.nabmart.domain.review.repository.ReviewRepository;
import com.prgrms.nabmart.domain.review.service.request.RegisterReviewCommand;
import com.prgrms.nabmart.domain.review.service.request.UpdateReviewCommand;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByUserResponse;
import com.prgrms.nabmart.domain.review.service.response.FindReviewsByUserResponse.FindReviewByUserResponse;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long registerReview(
        RegisterReviewCommand registerReviewCommand
    ) {
        User foundUser = userRepository.findById(registerReviewCommand.userId())
            .orElseThrow(() -> new NotFoundUserException("존재하지 않은 사용자입니다."));

        Item foundItem = itemRepository.findById(registerReviewCommand.itemId())
            .orElseThrow(() -> new NotFoundItemException("존재하지 않는 상품입니다."));

        Review review = Review.builder()
            .user(foundUser)
            .item(foundItem)
            .rate(registerReviewCommand.rate())
            .content(registerReviewCommand.content())
            .build();

        Review savedReview = reviewRepository.save(review);

        return savedReview.getReviewId();
    }

    @Transactional
    public void deleteReview(
        final Long reviewId
    ) {
        Review foundReview = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundReviewException("해당 리뷰를 찾을 수 없습니다."));

        reviewRepository.delete(foundReview);
    }

    @Transactional
    public void updateReview(
        UpdateReviewCommand updateReviewCommand
    ) {
        Review foundReview = reviewRepository.findById(updateReviewCommand.reviewId())
            .orElseThrow(() -> new NotFoundReviewException("해당 리뷰를 찾을 수 없습니다."));

        foundReview.changeRageAndContent(updateReviewCommand.rate(), updateReviewCommand.content());
    }

    @Transactional(readOnly = true)
    public FindReviewsByUserResponse findReviewsByUser(
        final Long userId
    ) {
        User foundUser = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("해당 사용자를 찾을 수 없습니다."));

        List<Review> foundReviews = reviewRepository.findAllByUserOrderByCreatedAt(
            foundUser);

        return FindReviewsByUserResponse.from(
            foundReviews
                .stream()
                .map(
                    review -> FindReviewByUserResponse.of(
                        review.getReviewId(),
                        foundUser.getNickname(),
                        review.getContent(),
                        review.getCreatedAt()
                    )
                )
                .toList());
    }
}
