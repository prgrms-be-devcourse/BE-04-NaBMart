package com.prgrms.nabmart.domain.coupon;

import com.prgrms.nabmart.domain.coupon.exception.InvalidUsedCouponException;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponId;

    @Column(nullable = false)
    private boolean isUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public UserCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
        isUsed = false;
    }

    public void use() {
        if (isUsed == true) {
            throw new InvalidUsedCouponException("이미 사용한 쿠폰입니다.");
        }
        isUsed = true;
    }
}

