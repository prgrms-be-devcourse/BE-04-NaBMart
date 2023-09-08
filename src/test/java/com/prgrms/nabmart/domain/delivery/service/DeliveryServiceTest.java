package com.prgrms.nabmart.domain.delivery.service;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.deliveringOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.prgrms.nabmart.domain.delivery.Delivery;
import com.prgrms.nabmart.domain.delivery.exception.NotFoundDeliveryException;
import com.prgrms.nabmart.domain.delivery.repository.DeliveryRepository;
import com.prgrms.nabmart.domain.delivery.service.request.FindDeliveryCommand;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.delivery.support.DeliveryFixture;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.user.User;
import com.prgrms.nabmart.domain.user.UserGrade;
import com.prgrms.nabmart.domain.user.UserRole;
import com.prgrms.nabmart.domain.user.exception.NotFoundUserException;
import com.prgrms.nabmart.domain.user.repository.UserRepository;
import com.prgrms.nabmart.domain.user.support.UserFixture;
import com.prgrms.nabmart.global.auth.exception.AuthorizationException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    DeliveryService deliveryService;

    @Mock
    DeliveryRepository deliveryRepository;

    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("findDelivery 메서드 실행 시")
    class FindDeliveryTest {

        User user = UserFixture.user();
        Order order = deliveringOrder(1L, user);
        Delivery delivery = DeliveryFixture.delivery(order);
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
            assertThat(findDeliveryDetailResponse.address())
                .isEqualTo(delivery.getAddress());
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
}
