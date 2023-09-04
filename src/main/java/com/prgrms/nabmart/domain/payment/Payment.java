package com.prgrms.nabmart.domain.payment;

import com.prgrms.nabmart.domain.BaseTimeEntity;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    @Setter
    private String paymentKey;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
