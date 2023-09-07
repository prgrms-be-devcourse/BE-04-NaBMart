package com.prgrms.nabmart.domain.coupon.repository;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserAndCoupon(User user, Coupon coupon);

    @Query("SELECT uc FROM UserCoupon uc JOIN FETCH uc.coupon c "
        + "WHERE uc.user = :user AND uc.isUsed = :isUsed AND c.endAt > :currentDate")
    List<UserCoupon> findByUserAndIsUsedAndCouponEndAtAfter(
        @Param("user") User user,
        @Param("isUsed") boolean isUsed,
        @Param("currentDate") LocalDate currentDate);
}

