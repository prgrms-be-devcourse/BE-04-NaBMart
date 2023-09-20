package com.prgrms.nabmart.domain.delivery;

import com.prgrms.nabmart.domain.delivery.exception.AlreadyAssignedDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.InvalidDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.UnauthorizedDeliveryException;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.OrderStatus;
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
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

    private static final int ADDRESS_LENGTH = 500;
    private static final int ZERO = 0;
    public static final String DELETED = "삭제됨";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "riderId")
    private Rider rider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column
    private LocalDateTime arrivedAt;

    @Version
    private Long version;

    @Column
    private String address;

    @Column
    private Integer orderPrice;

    @Column
    private String riderRequest;

    @Column
    private Integer deliveryFee;

    @Builder
    public Delivery(final Order order, int estimateMinutes) {
        this.order = order;
        this.deliveryStatus = DeliveryStatus.ACCEPTING_ORDER;
        this.address = order.getAddress();
        this.orderPrice = order.getPrice();
        this.riderRequest = order.getRiderRequest();
        this.deliveryFee = order.getDeliveryFee();
        this.arrivedAt = LocalDateTime.now().plusMinutes(estimateMinutes);
        order.updateOrderStatus(OrderStatus.DELIVERING);
    }

    public boolean isOwnByUser(final User user) {
        return this.order.isOwnByUser(user);
    }

    public void startDelivery(final int estimateMinutes) {
        validateEstimateMinutes(estimateMinutes);
        this.arrivedAt = LocalDateTime.now().plusMinutes(estimateMinutes);
        this.deliveryStatus = DeliveryStatus.START_DELIVERY;
    }

    private void validateEstimateMinutes(final int estimateMinutes) {
        if(estimateMinutes < ZERO) {
            throw new InvalidDeliveryException("배송 예상 소요 시간은 음수일 수 없습니다.");
        }
    }

    public void checkAuthority(final Rider rider) {
        if (!this.rider.equals(rider)) {
            throw new UnauthorizedDeliveryException("권한이 없습니다.");
        }
    }

    public void assignRider(Rider rider) {
        checkAlreadyAssignedToRider();
        this.rider = rider;
    }

    private void checkAlreadyAssignedToRider() {
        if (Objects.nonNull(this.rider)) {
            throw new AlreadyAssignedDeliveryException("이미 배차 완료된 배달입니다.");
        }
    }

    public void completeDelivery() {
        this.arrivedAt = LocalDateTime.now();
        this.deliveryStatus = DeliveryStatus.DELIVERED;
    }

    public void deleteAboutUser() {
        this.order = null;
        this.address = DELETED;
    }
}
