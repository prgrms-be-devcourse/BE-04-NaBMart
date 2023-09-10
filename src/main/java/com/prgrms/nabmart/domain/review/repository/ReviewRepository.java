package com.prgrms.nabmart.domain.review.repository;

import com.prgrms.nabmart.domain.item.Item;
import com.prgrms.nabmart.domain.review.Review;
import com.prgrms.nabmart.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByUserOrderByCreatedAt(User user);

    List<Review> findAllByItemOrderByCreatedAt(Item item);
}
