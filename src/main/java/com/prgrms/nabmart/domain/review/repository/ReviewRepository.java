package com.prgrms.nabmart.domain.review.repository;

import com.prgrms.nabmart.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
