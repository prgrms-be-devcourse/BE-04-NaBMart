package com.prgrms.nabmart.domain.delivery;

import com.prgrms.nabmart.domain.delivery.exception.InvalidDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.UnauthorizedDeliveryException;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.global.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "riderId")
    private Rider rider;

    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column
    private LocalDateTime arrivedAt;

    @Builder
    public Delivery(final Order order) {
        this.order = order;
        this.deliveryStatus = DeliveryStatus.ACCEPTING_ORDER;
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
        this.rider = rider;
    }

    public void completeDelivery() {
        this.arrivedAt = LocalDateTime.now();
        this.deliveryStatus = DeliveryStatus.DELIVERED;
    }
}
