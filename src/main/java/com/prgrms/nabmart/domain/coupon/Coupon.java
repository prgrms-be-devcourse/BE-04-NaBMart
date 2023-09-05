package com.prgrms.nabmart.domain.coupon;

import com.prgrms.nabmart.domain.coupon.exception.InvalidCouponException;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(nullable = false)
    private LocalDate endAt;

    @OneToOne(mappedBy = "coupon")
    private Order order;

    @Builder
    public Coupon(Integer discount, String name, String description, Integer minOrderPrice,
        LocalDate endAt) {
        validateEndAt(endAt);
        this.discount = discount;
        this.name = name;
        this.description = description;
        this.minOrderPrice = minOrderPrice;
        this.endAt = endAt;
    }

    private void validateEndAt(LocalDate endAt) {
        LocalDate currentDate = LocalDate.now();
        if (endAt.isBefore(currentDate)) {
            throw new InvalidCouponException("쿠폰 종료일은 현재 날짜보다 이전일 수 없습니다.");
        }
    }
}
