package com.prgrms.nabmart.domain.coupon.repository;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserAndCoupon(User user, Coupon coupon);

}
