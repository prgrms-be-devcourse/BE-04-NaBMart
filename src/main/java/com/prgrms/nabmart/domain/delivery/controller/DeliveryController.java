package com.prgrms.nabmart.domain.delivery.controller;

import com.prgrms.nabmart.domain.delivery.controller.request.FindRiderDeliveriesRequest;
import com.prgrms.nabmart.domain.delivery.controller.request.RegisterDeliveryRequest;
import com.prgrms.nabmart.domain.delivery.controller.request.StartDeliveryRequest;
import com.prgrms.nabmart.domain.delivery.exception.AlreadyAssignedDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.DeliveryException;
import com.prgrms.nabmart.domain.delivery.service.DeliveryService;
import com.prgrms.nabmart.domain.delivery.service.request.AcceptDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.CompleteDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindRiderDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindWaitingDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.RegisterDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.StartDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindRiderDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DeliveryController {

    private static final String BASE_URI = "/api/v1/deliveries/";

    private final DeliveryService deliveryService;

    @PostMapping("/orders/{orderId}/deliveries")
    public ResponseEntity<Void> registerDelivery(
        @PathVariable Long orderId,
        @RequestBody RegisterDeliveryRequest registerDeliveryRequest,
        @LoginUser Long userId) {
        RegisterDeliveryCommand registerDeliveryCommand = RegisterDeliveryCommand.of(
            orderId,
            userId,
            registerDeliveryRequest.estimateMinutes());
        Long deliveryId = deliveryService.registerDelivery(registerDeliveryCommand);
        URI location = URI.create(BASE_URI + deliveryId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/orders/{orderId}/deliveries")
    public ResponseEntity<FindDeliveryDetailResponse> findDelivery(
        @PathVariable final Long orderId,
        @LoginUser final Long userId) {
        FindDeliveryCommand findDeliveryCommand = FindDeliveryCommand.of(userId, orderId);
        FindDeliveryDetailResponse findDeliveryDetailResponse
            = deliveryService.findDelivery(findDeliveryCommand);
        return ResponseEntity.ok(findDeliveryDetailResponse);
    }

    @PatchMapping("/deliveries/{deliveryId}/accept")
    public ResponseEntity<Void> acceptDelivery(
        @PathVariable final Long deliveryId,
        @LoginUser final Long riderId) {
        AcceptDeliveryCommand acceptDeliveryCommand = AcceptDeliveryCommand.of(deliveryId, riderId);
        acceptDeliveryIfNotAssigned(acceptDeliveryCommand);
        return ResponseEntity.noContent().build();
    }

    private void acceptDeliveryIfNotAssigned(AcceptDeliveryCommand acceptDeliveryCommand) {
        try {
            deliveryService.acceptDelivery(acceptDeliveryCommand);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new AlreadyAssignedDeliveryException("이미 배차 완료된 주문입니다.");
        }
    }

    @PatchMapping("/deliveries/{deliveryId}/pickup")
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

    @PatchMapping("/deliveries/{deliveryId}/complete")
    public ResponseEntity<Void> completeDelivery(
        @PathVariable final Long deliveryId,
        @LoginUser final Long riderId) {
        CompleteDeliveryCommand completeDeliveryCommand
            = CompleteDeliveryCommand.of(deliveryId, riderId);
        deliveryService.completeDelivery(completeDeliveryCommand);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deliveries/waiting")
    public ResponseEntity<FindWaitingDeliveriesResponse> findWaitingDeliveries(
        final Pageable pageable) {
        FindWaitingDeliveriesCommand findWaitingDeliveriesCommand
            = FindWaitingDeliveriesCommand.from(pageable);
        FindWaitingDeliveriesResponse findWaitingDeliveriesResponse
            = deliveryService.findWaitingDeliveries(findWaitingDeliveriesCommand);
        return ResponseEntity.ok(findWaitingDeliveriesResponse);
    }

    @GetMapping("/deliveries")
    public ResponseEntity<FindRiderDeliveriesResponse> findRiderDeliveries(
        FindRiderDeliveriesRequest findRiderDeliveriesRequest,
        final Pageable pageable,
        @LoginUser final Long riderId) {
        FindRiderDeliveriesCommand findRiderDeliveriesCommand = FindRiderDeliveriesCommand.of(
            riderId,
            findRiderDeliveriesRequest.deliveryStatuses(),
            pageable);
        FindRiderDeliveriesResponse findRiderDeliveriesResponse
            = deliveryService.findRiderDeliveries(findRiderDeliveriesCommand);
        return ResponseEntity.ok(findRiderDeliveriesResponse);
    }

    @ExceptionHandler(DeliveryException.class)
    public ResponseEntity<ErrorTemplate> deliveryExHandle(DeliveryException ex) {
        ErrorTemplate errorTemplate = ErrorTemplate.of(ex.getMessage());
        return ResponseEntity.badRequest().body(errorTemplate);
    }

    @ExceptionHandler(AlreadyAssignedDeliveryException.class)
    public ResponseEntity<ErrorTemplate> alreadyAssignedDeliveryExHandel(
        AlreadyAssignedDeliveryException ex) {
        ErrorTemplate errorTemplate = ErrorTemplate.of(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorTemplate);
    }
}
