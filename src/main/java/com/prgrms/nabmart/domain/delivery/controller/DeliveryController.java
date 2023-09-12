package com.prgrms.nabmart.domain.delivery.controller;

import com.prgrms.nabmart.domain.delivery.controller.request.StartDeliveryRequest;
import com.prgrms.nabmart.domain.delivery.service.DeliveryService;
import com.prgrms.nabmart.domain.delivery.service.request.AcceptDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.CompleteDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindWaitingDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.StartDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{orderId}")
    public ResponseEntity<FindDeliveryDetailResponse> findDelivery(
        @PathVariable final Long orderId,
        @LoginUser final Long userId) {
        FindDeliveryCommand findDeliveryCommand = FindDeliveryCommand.of(userId, orderId);
        FindDeliveryDetailResponse findDeliveryDetailResponse
            = deliveryService.findDelivery(findDeliveryCommand);
        return ResponseEntity.ok(findDeliveryDetailResponse);
    }

    @PatchMapping("/{deliveryId}/accept")
    public ResponseEntity<Void> acceptDelivery(
        @PathVariable final Long deliveryId,
        @LoginUser final Long riderId) {
        AcceptDeliveryCommand acceptDeliveryCommand = AcceptDeliveryCommand.of(deliveryId, riderId);
        deliveryService.acceptDelivery(acceptDeliveryCommand);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{deliveryId}/pickup")
    public ResponseEntity<Void> startDelivery(
        @PathVariable final Long deliveryId,
        @RequestBody @Valid StartDeliveryRequest startDeliveryRequest,
        @LoginUser final Long riderId) {
        StartDeliveryCommand startDeliveryCommand = StartDeliveryCommand.of(
            deliveryId,
            startDeliveryRequest.deliveryEstimateMinutes(),
            riderId);
        deliveryService.startDelivery(startDeliveryCommand);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{deliveryId}/complete")
    public ResponseEntity<Void> completeDelivery(
        @PathVariable final Long deliveryId,
        @LoginUser final Long riderId) {
        CompleteDeliveryCommand completeDeliveryCommand
            = CompleteDeliveryCommand.of(deliveryId, riderId);
        deliveryService.completeDelivery(completeDeliveryCommand);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/waiting")
    public ResponseEntity<FindWaitingDeliveriesResponse> findWaitingDeliveries(
        final Pageable pageable) {
        FindWaitingDeliveriesCommand findWaitingDeliveriesCommand
            = FindWaitingDeliveriesCommand.from(pageable);
        FindWaitingDeliveriesResponse findWaitingDeliveriesResponse
            = deliveryService.findWaitingDeliveries(findWaitingDeliveriesCommand);
        return ResponseEntity.ok(findWaitingDeliveriesResponse);
    }
}
