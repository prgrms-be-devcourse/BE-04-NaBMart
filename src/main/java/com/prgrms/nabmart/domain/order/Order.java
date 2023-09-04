package com.prgrms.nabmart.domain.order;

import com.prgrms.nabmart.domain.BaseTimeEntity;
import com.prgrms.nabmart.domain.coupon.Coupon;
import com.prgrms.nabmart.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "orders")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING; // 주문 상태 정보, 기본값 'PENDING'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
