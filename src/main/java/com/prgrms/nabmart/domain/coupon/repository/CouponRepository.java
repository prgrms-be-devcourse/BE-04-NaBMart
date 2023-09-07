package com.prgrms.nabmart.domain.coupon.repository;

import com.prgrms.nabmart.domain.coupon.Coupon;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findByEndAtGreaterThan(LocalDate currentDate);

}


