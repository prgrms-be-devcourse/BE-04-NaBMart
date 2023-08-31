package com.prgrms.nabmart.domain.coupon;

import com.prgrms.nabmart.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CouponId;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(nullable = false)
    private LocalDate endAt;
}
