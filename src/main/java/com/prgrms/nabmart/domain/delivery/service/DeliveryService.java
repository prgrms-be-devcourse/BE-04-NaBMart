package com.prgrms.nabmart.domain.delivery.service;

import static com.prgrms.nabmart.domain.notification.NotificationMessage.COMPLETE_DELIVERY;
import static com.prgrms.nabmart.domain.notification.NotificationMessage.REGISTER_DELIVERY;
import static com.prgrms.nabmart.domain.notification.NotificationMessage.START_DELIVERY;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.controller.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.delivery.exception.AlreadyRegisteredDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.NotFoundDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.NotFoundRiderException;
import com.prgrms.nabmart.domain.delivery.exception.UnauthorizedDeliveryException;
import com.prgrms.nabmart.domain.delivery.repository.DeliveryRepository;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.delivery.service.request.AcceptDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.CompleteDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryByOrderCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryDetailCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindRiderDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindWaitingDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.RegisterDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.StartDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryByOrderResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindRiderDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse;
import com.prgrms.nabmart.domain.notification.NotificationType;
import com.prgrms.nabmart.domain.notification.service.NotificationService;
import com.prgrms.nabmart.domain.notification.service.request.SendNotificationCommand;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.exception.NotFoundOrderException;
import com.prgrms.nabmart.domain.order.repository.OrderRepository;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final RiderRepository riderRepository;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    @Transactional
    public Long registerDelivery(RegisterDeliveryCommand registerDeliveryCommand) {
        checkUserHasRegisterDeliveryAuthority(registerDeliveryCommand.userId());
        Order order = findOrderByOrderIdPessimistic(registerDeliveryCommand);
        checkAlreadyRegisteredDelivery(order);
        Delivery delivery = new Delivery(order, registerDeliveryCommand.estimateMinutes());
        deliveryRepository.save(delivery);

        sendRegisterDeliveryNotification(registerDeliveryCommand, delivery, order);

        return delivery.getDeliveryId();
    }

    private void sendRegisterDeliveryNotification(
        RegisterDeliveryCommand registerDeliveryCommand,
        Delivery delivery,
        Order order) {
        SendNotificationCommand notificationCommand = SendNotificationCommand.of(
            delivery.getUserId(),
            REGISTER_DELIVERY.getTitle(),
            REGISTER_DELIVERY.getContentFromFormat(
                order.getName(),
                registerDeliveryCommand.estimateMinutes()),
            NotificationType.DELIVERY);
        notificationService.sendNotification(notificationCommand);
    }

    private void checkUserHasRegisterDeliveryAuthority(final Long userId) {
        User user = findUserByUserId(userId);
        if (!user.isEmployee()) {
            throw new UnauthorizedDeliveryException("권한이 없습니다.");
        }
    }

    private Order findOrderByOrderIdPessimistic(RegisterDeliveryCommand registerDeliveryCommand) {
        return orderRepository.findByIdPessimistic(registerDeliveryCommand.orderId())
            .orElseThrow(() -> new NotFoundOrderException("존재하지 않는 주문입니다."));
    }

    private void checkAlreadyRegisteredDelivery(final Order order) {
        if (deliveryRepository.existsByOrder(order)) {
            throw new AlreadyRegisteredDeliveryException("이미 배달이 생성된 주문입니다.");
        }
    }

    @Transactional(readOnly = true)
    public FindDeliveryByOrderResponse findDeliveryByOrder(
        FindDeliveryByOrderCommand findDeliveryByOrderCommand) {
        User user = findUserByUserId(findDeliveryByOrderCommand.userId());
        Delivery delivery = findDeliveryByOrderWithOrder(findDeliveryByOrderCommand.orderId());
        checkAuthority(delivery, user);
        return FindDeliveryByOrderResponse.from(delivery);
    }

    private void checkAuthority(final Delivery delivery, final User user) {
        if (!delivery.isOwnByUser(user)) {
            throw new UnauthorizedDeliveryException("권한이 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public FindDeliveryDetailResponse findDelivery(
        FindDeliveryDetailCommand findDeliveryDetailCommand) {
        Delivery delivery = findDeliveryByDeliveryIdWithOrderAndOrderItems(
            findDeliveryDetailCommand);
        return FindDeliveryDetailResponse.from(delivery);
    }

    private Delivery findDeliveryByDeliveryIdWithOrderAndOrderItems(
        FindDeliveryDetailCommand findDeliveryDetailCommand) {
        return deliveryRepository.findByIdWithOrderAndItems(
                findDeliveryDetailCommand.deliveryId())
            .orElseThrow(() -> new NotFoundDeliveryException("존재하지 않는 배달입니다."));
    }

    @Transactional
    public void acceptDelivery(AcceptDeliveryCommand acceptDeliveryCommand) {
        Rider rider = findRiderByRiderId(acceptDeliveryCommand.riderId());
        Delivery delivery = findDeliveryByDeliveryIdOptimistic(acceptDeliveryCommand);
        delivery.assignRider(rider);
    }

    private Delivery findDeliveryByDeliveryIdOptimistic(
        AcceptDeliveryCommand acceptDeliveryCommand) {
        return deliveryRepository.findByIdOptimistic(acceptDeliveryCommand.deliveryId())
            .orElseThrow(() -> new NotFoundDeliveryException("존재하지 않는 배달입니다."));
    }

    @Transactional
    public void startDelivery(StartDeliveryCommand startDeliveryCommand) {
        Rider rider = findRiderByRiderId(startDeliveryCommand.riderId());
        Delivery delivery = findDeliveryByDeliveryId(startDeliveryCommand.deliveryId());
        delivery.checkAuthority(rider);
        delivery.startDelivery(startDeliveryCommand.deliveryEstimateMinutes());

        sendStartDeliveryNotification(startDeliveryCommand, delivery);
    }

    private void sendStartDeliveryNotification(
        StartDeliveryCommand startDeliveryCommand,
        Delivery delivery) {
        SendNotificationCommand notificationCommand = SendNotificationCommand.of(
            delivery.getUserId(),
            START_DELIVERY.getTitle(),
            START_DELIVERY.getContentFromFormat(startDeliveryCommand.deliveryEstimateMinutes()),
            NotificationType.DELIVERY);
        notificationService.sendNotification(notificationCommand);
    }

    @Transactional
    public void completeDelivery(CompleteDeliveryCommand completeDeliveryCommand) {
        Rider rider = findRiderByRiderId(completeDeliveryCommand.riderId());
        Delivery delivery = findDeliveryByDeliveryId(completeDeliveryCommand.deliveryId());
        delivery.checkAuthority(rider);
        delivery.completeDelivery();

        sendCompleteDeliveryNotification(delivery);
    }

    private void sendCompleteDeliveryNotification(Delivery delivery) {
        SendNotificationCommand notificationCommand = SendNotificationCommand.of(
            delivery.getUserId(),
            COMPLETE_DELIVERY.getTitle(),
            COMPLETE_DELIVERY.getContentFromFormat(),
            NotificationType.DELIVERY);
        notificationService.sendNotification(notificationCommand);
    }

    private Rider findRiderByRiderId(final Long riderId) {
        return riderRepository.findById(riderId)
            .orElseThrow(() -> new NotFoundRiderException("존재하지 않는 라이더입니다."));
    }

    @Transactional(readOnly = true)
    public FindWaitingDeliveriesResponse findWaitingDeliveries(
        FindWaitingDeliveriesCommand findWaitingDeliveriesCommand) {
        Page<Delivery> deliveriesPage
            = deliveryRepository.findWaitingDeliveries(findWaitingDeliveriesCommand.pageable());
        return FindWaitingDeliveriesResponse.from(deliveriesPage);
    }

    @Transactional(readOnly = true)
    public FindRiderDeliveriesResponse findRiderDeliveries(
        FindRiderDeliveriesCommand findRiderDeliveriesCommand) {
        Rider rider = findRiderByRiderId(findRiderDeliveriesCommand.riderId());
        Page<Delivery> deliveriesPage = deliveryRepository.findRiderDeliveries(
            rider,
            findRiderDeliveriesCommand.deliveryStatuses(),
            findRiderDeliveriesCommand.pageable());
        return FindRiderDeliveriesResponse.of(
            deliveriesPage.getContent(),
            deliveriesPage.getNumber(),
            deliveriesPage.getTotalElements());
    }

    private User findUserByUserId(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundUserException("존재하지 않는 유저입니다."));
    }

    private Delivery findDeliveryByOrderWithOrder(final Long orderId) {
        return deliveryRepository.findByOrderIdWithOrder(orderId)
            .orElseThrow(() -> new NotFoundDeliveryException("존재하지 않는 배달입니다."));
    }

    private Delivery findDeliveryByDeliveryId(final Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NotFoundDeliveryException("존재하지 않는 배달입니다."));
    }
}
