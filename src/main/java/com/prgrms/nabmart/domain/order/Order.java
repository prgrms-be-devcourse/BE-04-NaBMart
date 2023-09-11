package com.prgrms.nabmart.domain.order;

import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.order.exception.InvalidOrderException;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderItemException;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@Builder
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

    @Column
    private String address;

    @Column
    private String riderRequest;

    @Column
    private Integer deliveryFee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING; // 주문 상태 정보, 기본값 'PENDING'

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id")
    private UserCoupon userCoupon;

    public Order(final User user, final List<OrderItem> orderItems) {
        this.user = user;
        this.address = user.getAddress();
        validateOrderItems(orderItems);
        createOrderName(orderItems);
        setOrderItems(orderItems);
        calculateTotalPrice();
    }

    private void createOrderName(final List<OrderItem> orderItems) {
        this.name = (orderItems.size() == 1) ?
            orderItems.get(0).getItem().getName() :
            orderItems.get(0).getItem().getName() + "외 " + (orderItems.size() - 1) + "개";
    }

    private void setOrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(this);
        }
    }

    private void calculateTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.calculateSubtotal();
        }
        this.price = totalPrice;
        calculateDeliveryFee(totalPrice);
    }

    private void calculateDeliveryFee(final int totalPrice) {
        if (totalPrice >= 43000) {
            this.deliveryFee = 0;
        } else if (totalPrice >= 15000) {
            this.deliveryFee = 3000;
        } else {
            throw new InvalidOrderException("주문 최소 금액은 15000원 이상 입니다");
        }
    }

    private void validateOrderItems(final List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new NotFoundOrderItemException("주문 아이템이 비어 있습니다.");
        }
    }

    public boolean isOwnByUser(final User user) {
        return this.user.equals(user);
    }
}
