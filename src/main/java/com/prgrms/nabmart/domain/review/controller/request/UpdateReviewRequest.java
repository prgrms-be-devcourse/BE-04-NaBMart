package com.prgrms.nabmart.domain.review.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record UpdateReviewRequest(
    @Range(min = 0, max = 5, message = "평점은 0~5 사이로 매길 수 있습니다.") double rate,
    @NotBlank(message = "리뷰 내용은 필수 입력 항목입니다.") @Size(min = 1, max = 100, message = "리뷰 내용은 최소 1자 이상 최대 100자 이하입니다.") String content
) {

    public static UpdateReviewRequest of(
        final double rate,
        final String content
    ) {
        return new UpdateReviewRequest(rate, content);
    }
}
