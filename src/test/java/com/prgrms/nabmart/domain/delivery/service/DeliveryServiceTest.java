package com.prgrms.nabmart.domain.delivery.service;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.deliveringOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.delivery.Rider;
import com.prgrms.nabmart.domain.delivery.exception.InvalidDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.NotFoundDeliveryException;
import com.prgrms.nabmart.domain.delivery.exception.NotFoundRiderException;
import com.prgrms.nabmart.domain.delivery.exception.UnauthorizedDeliveryException;
import com.prgrms.nabmart.domain.delivery.repository.DeliveryRepository;
import com.prgrms.nabmart.domain.delivery.repository.RiderRepository;
import com.prgrms.nabmart.domain.delivery.service.request.AcceptDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.CompleteDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.request.FindWaitingDeliveriesCommand;
import com.prgrms.nabmart.domain.delivery.service.request.StartDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.support.DeliveryFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.support.OrderFixture;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import com.prgrms.nabmart.global.auth.exception.AuthorizationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    DeliveryService deliveryService;

    @Mock
    DeliveryRepository deliveryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RiderRepository riderRepository;

    User user = UserFixture.user();
    Order order = deliveringOrder(1L, user);
    Rider rider = DeliveryFixture.rider();
    Delivery delivery = DeliveryFixture.acceptedDelivery(order, rider);
    TemporalUnitOffset withInOneSeconds = within(1, ChronoUnit.SECONDS);

    @Nested
    @DisplayName("findDelivery 메서드 실행 시")
    class FindDeliveryTest {

        FindDeliveryCommand findDeliveryCommand = DeliveryFixture.findDeliveryCommand();

        @Test
        @DisplayName("성공")
        void success() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            given(deliveryRepository.findByOrderIdWithOrder(any()))
                .willReturn(Optional.ofNullable(delivery));

            //when
            FindDeliveryDetailResponse findDeliveryDetailResponse
                = deliveryService.findDelivery(findDeliveryCommand);

            //then
            assertThat(findDeliveryDetailResponse.deliveryStatus())
                .isEqualTo(delivery.getDeliveryStatus());
            assertThat(findDeliveryDetailResponse.arrivedAt())
                .isEqualTo(delivery.getArrivedAt());
            assertThat(findDeliveryDetailResponse.name())
                .isEqualTo(delivery.getOrder().getName());
            assertThat(findDeliveryDetailResponse.price())
                .isEqualTo(delivery.getOrder().getPrice());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 User")
        void throwExceptionWhenNotFoundUser() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> deliveryService.findDelivery(findDeliveryCommand))
                .isInstanceOf(NotFoundUserException.class);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 Delivery")
        void throwExceptionWhenNotFoundDelivery() {
            //given
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            given(deliveryRepository.findByOrderIdWithOrder(any()))
                .willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> deliveryService.findDelivery(findDeliveryCommand))
                .isInstanceOf(NotFoundDeliveryException.class);
        }

        @Test
        @DisplayName("예외: 로그인 User와 일치하지 않는 Order의 User")
        void throwExceptionWhenNotEqualsLoginUserAndOrderUser() {
            //given
            String notEqualsProvider = "notEqualsProvider";
            String notEqualsProviderId = "notEqualsProviderId";
            User notEqualsLoginUser = User.builder()
                .provider(notEqualsProvider)
                .providerId(notEqualsProviderId)
                .nickname("nickname")
                .email("email@email.com")
                .userGrade(UserGrade.NORMAL)
                .userRole(UserRole.ROLE_USER)
                .build();

            given(userRepository.findById(any()))
                .willReturn(Optional.ofNullable(notEqualsLoginUser));
            given(deliveryRepository.findByOrderIdWithOrder(any()))
                .willReturn(Optional.ofNullable(delivery));

            //when
            //then
            assertThatThrownBy(() -> deliveryService.findDelivery(findDeliveryCommand))
                .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("startDelivery 메서드 실행 시")
    class StartDeliveryTest {

        Delivery delivery = DeliveryFixture.acceptedDelivery(order, rider);

        @Test
        @DisplayName("성공: 배달시작으로 상태 변경")
        void success() {
            //given
            int deliveryEstimateMinutes = 20;
            StartDeliveryCommand startDeliveryCommand
                = StartDeliveryCommand.of(1L, deliveryEstimateMinutes, 1L);

            given(riderRepository.findById(any())).willReturn(Optional.ofNullable(rider));
            given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.startDelivery(startDeliveryCommand);

            //then
            LocalDateTime estimatedArrivedAt = LocalDateTime.now()
                .plusMinutes(deliveryEstimateMinutes);
            assertThat(delivery.getArrivedAt()).isCloseTo(estimatedArrivedAt, withInOneSeconds);
            assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.START_DELIVERY);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 라이더")
        void throwExceptionWhenNotFoundRider() {
            //given
            StartDeliveryCommand startDeliveryCommand
                = new StartDeliveryCommand(1L, 20, 1L);

            given(riderRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> deliveryService.startDelivery(startDeliveryCommand))
                .isInstanceOf(NotFoundRiderException.class);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 배달")
        void throwExceptionWhenNotFoundDelivery() {
            //given
            StartDeliveryCommand startDeliveryCommand = StartDeliveryCommand.of(
                1L,
                10,
                1L);

            given(riderRepository.findById(any())).willReturn(Optional.ofNullable(rider));
            given(deliveryRepository.findById(any())).willReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> deliveryService.startDelivery(startDeliveryCommand))
                .isInstanceOf(NotFoundDeliveryException.class);
        }

        @Test
        @DisplayName("예외: 권한이 없는 라이더")
        void throwExceptionWhenUnauthorizedRider() {
            //given
            Rider unauthorizedRider = new Rider("unauthorized", "password123", "address");
            StartDeliveryCommand startDeliveryCommand = new StartDeliveryCommand(1L, 20, 1L);

            given(riderRepository.findById(any())).willReturn(Optional.of(unauthorizedRider));
            given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(delivery));

            //when
            assertThatThrownBy(() -> deliveryService.startDelivery(startDeliveryCommand))
                .isInstanceOf(UnauthorizedDeliveryException.class);
        }

        @Test
        @DisplayName("예외: 배달 소요 시간이 음수")
        void throwExceptionWhenDeliveryEstimateMinutesIsMinus() {
            //given
            int invalidDeliveryEstimateMinutes = -1;
            StartDeliveryCommand startDeliveryCommand
                = StartDeliveryCommand.of(1L, invalidDeliveryEstimateMinutes, 1L);

            given(riderRepository.findById(any())).willReturn(Optional.ofNullable(rider));
            given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(delivery));

            //when
            assertThatThrownBy(() -> deliveryService.startDelivery(startDeliveryCommand))
                .isInstanceOf(InvalidDeliveryException.class);
        }
    }

    @Nested
    @DisplayName("completeDelivery 메서드 실행 시")
    class CompleteDeliveryTest {

        CompleteDeliveryCommand completeDeliveryCommand
            = new CompleteDeliveryCommand(1L, 1L);

        @Test
        @DisplayName("성공")
        void success() {
            //given
            given(riderRepository.findById(any())).willReturn(Optional.ofNullable(rider));
            given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.completeDelivery(completeDeliveryCommand);

            //then
            LocalDateTime now = LocalDateTime.now();
            assertThat(delivery.getArrivedAt()).isCloseTo(now, withInOneSeconds);
            assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 라이더")
        void throwExceptionWhenNotFoundRider() {
            //given
            given(riderRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> deliveryService.completeDelivery(completeDeliveryCommand))
                .isInstanceOf(NotFoundRiderException.class);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 배달")
        void throwExceptionWhenNotFoundDelivery() {
            //given
            given(riderRepository.findById(any())).willReturn(Optional.ofNullable(rider));
            given(deliveryRepository.findById(any())).willReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> deliveryService.completeDelivery(completeDeliveryCommand))
                .isInstanceOf(NotFoundDeliveryException.class);
        }

        @Test
        @DisplayName("예외: 권한이 없는 라이더")
        void throwExceptionWhenUnauthorizedRider() {
            //given
            Rider unauthorizedRider
                = new Rider("unauthorized", "password", "address");
            given(riderRepository.findById(any())).willReturn(Optional.of(unauthorizedRider));
            given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(delivery));

            //when
            assertThatThrownBy(() -> deliveryService.completeDelivery(completeDeliveryCommand))
                .isInstanceOf(UnauthorizedDeliveryException.class);
        }
    }

    @Nested
    @DisplayName("findWaitingDeliveries 메서드 실행 시")
    class FindWaitingDeliveriesTest {

        private List<Order> createOrders(int end) {
            return IntStream.range(0, end)
                .mapToObj(i -> OrderFixture.deliveringOrder(i, user))
                .toList();
        }

        private List<Delivery> createDeliveries(int end) {
            return IntStream.range(0, end)
                .mapToObj(i -> {
                    Order completedOrder = OrderFixture.deliveringOrder(i, user);
                    return DeliveryFixture.waitingDelivery(completedOrder);
                }).toList();
        }

        @Test
        @DisplayName("성공")
        void success() {
            //given
            int totalElement = 3;
            int pageNumber = 0;
            List<Delivery> deliveries = createDeliveries(totalElement);
            PageImpl<Delivery> deliveriesPage = new PageImpl<>(deliveries);
            PageRequest pageRequest = PageRequest.of(pageNumber, totalElement);
            FindWaitingDeliveriesCommand findWaitingDeliveriesCommand = FindWaitingDeliveriesCommand.from(
                pageRequest);

            given(deliveryRepository.findWaitingDeliveries(any())).willReturn(deliveriesPage);

            //when
            FindWaitingDeliveriesResponse findWaitingDeliveriesResponse
                = deliveryService.findWaitingDeliveries(findWaitingDeliveriesCommand);

            //then
            assertThat(findWaitingDeliveriesResponse.totalElements()).isEqualTo(totalElement);
            assertThat(findWaitingDeliveriesResponse.page()).isEqualTo(pageNumber);
            assertThat(findWaitingDeliveriesResponse.deliveries()).hasSize(totalElement);
        }
    }

    @Nested
    @DisplayName("acceptDelivery 메서드 실행 시")
    class AcceptDeliveryTest {

        AcceptDeliveryCommand acceptDeliveryCommand = new AcceptDeliveryCommand(1L, 1L);
        Delivery delivery = DeliveryFixture.waitingDelivery(order);

        @Test
        @DisplayName("성공")
        void success() {
            //given
            given(riderRepository.findById(any())).willReturn(Optional.ofNullable(rider));
            given(deliveryRepository.findById(any())).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.acceptDelivery(acceptDeliveryCommand);

            //then
            assertThat(delivery.getRider()).isEqualTo(rider);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 라이더")
        void throwExceptionWhenNotFoundRider() {
            //given
            given(riderRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> deliveryService.acceptDelivery(acceptDeliveryCommand))
                .isInstanceOf(NotFoundRiderException.class);
        }

        @Test
        @DisplayName("예외: 존재하지 않는 배달")
        void throwExceptionWhenNotFoundDelivery() {
            //given
            given(riderRepository.findById(any())).willReturn(Optional.ofNullable(rider));
            given(deliveryRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> deliveryService.acceptDelivery(acceptDeliveryCommand))
                .isInstanceOf(NotFoundDeliveryException.class);
        }
    }
}
