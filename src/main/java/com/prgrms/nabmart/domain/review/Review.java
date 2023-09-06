package com.prgrms.nabmart.domain.review;


import static java.util.Objects.isNull;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.review.exception.InvalidReviewException;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    public static final int MAX_CONTENT = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    private double rate;

    @Column
    private String content;

    @Builder
    public Review(User user, Item item, double rate, String content) {
        validateUser(user);
        validateItem(item);
        validateRate(rate);
        validateContent(content);

        this.user = user;
        this.item = item;
        this.rate = rate;
        this.content = content;
    }

    public void validateUser(
        final User user
    ) {
        if (isNull(user)) {
            throw new NotFoundUserException("User 가 존재하지 않습니다.");
        }
    }

    public void validateItem(
        final Item item
    ) {
        if (isNull(item)) {
            throw new NotFoundItemException("Item 이 존재하지 않습니다.");
        }
    }

    public void validateRate(
        double rate
    ) {
        if (0 > rate) {
            throw new InvalidReviewException("평점은 양수여야 합니다.");
        }

        if (rate > 5) {
            rate = 5;
        }
    }

    public void validateContent(
        final String content
    ) {
        if (content.isBlank()) {
            throw new InvalidReviewException("리뷰 내용은 1자 이상이어야 합니다.");
        }

        if (content.length() > MAX_CONTENT) {
            throw new InvalidReviewException("리뷰 내용은 100자를 넘을 수 없습니다.");
        }
    }
}
