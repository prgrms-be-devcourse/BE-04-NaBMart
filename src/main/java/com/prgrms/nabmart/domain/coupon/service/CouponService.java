package com.prgrms.nabmart.domain.coupon.service;

import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.coupon.repository.CouponRepository;
import com.prgrms.nabmart.domain.coupon.service.request.RegisterCouponCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    //관리자 쿠폰 등록
    @Transactional
    public Long createCoupon(RegisterCouponCommand command) {
        Coupon coupon = Coupon.builder()
            .name(command.name())
            .discount(command.discount())
            .description(command.description())
            .minOrderPrice(command.minOrderPrice())
            .endAt(command.endAt())
            .build();

        return couponRepository.save(coupon).getCouponId();
    }
}
