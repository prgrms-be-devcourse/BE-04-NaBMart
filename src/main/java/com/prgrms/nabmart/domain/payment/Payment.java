package com.prgrms.nabmart.domain.payment;

import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
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
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public Payment(PaymentType paymentType, User user, Order order) {
        this.paymentType = paymentType;
        this.user = user;
        this.order = order;
    }

    public void changeStatus(final PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isMisMatchPrice(final int amount) {
        return this.order.isMisMatchPrice(amount);
    }

    public boolean isMisMatchStatus(final PaymentStatus paymentStatus) {
        return this.paymentStatus != paymentStatus;
    }

    public boolean isMisMachType(String method) {
        return !this.paymentType.getValue().equals(method);
    }
}
