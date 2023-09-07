package com.prgrms.nabmart.domain.delivery;

import com.prgrms.nabmart.domain.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column
    private LocalDateTime finishedTime;

    @Column(nullable = false)
    private String address;

    @Builder
    public Delivery(final Order order, final LocalDateTime finishedTime, final String address) {
        this.order = order;
        this.finishedTime = finishedTime;
        this.address = address;
        this.deliveryStatus = DeliveryStatus.ACCEPTING_ORDERS;
    }
}
