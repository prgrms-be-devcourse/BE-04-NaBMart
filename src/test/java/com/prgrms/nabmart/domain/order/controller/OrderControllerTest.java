package com.prgrms.nabmart.domain.order.controller;

import static com.prgrms.nabmart.domain.coupon.support.CouponFixture.userCoupon;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.completedOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.createOrderRequest;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.createOrderResponse;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.orderDetailResponse;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.pendingOrder;
import static com.prgrms.nabmart.domain.user.support.UserFixture.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.coupon.UserCoupon;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.controller.request.CreateOrderRequest;
import com.prgrms.nabmart.domain.order.controller.request.FindPayedOrdersRequest;
import com.prgrms.nabmart.domain.order.service.response.FindPayedOrdersResponse;
import com.prgrms.nabmart.domain.order.service.response.FindPayedOrdersResponse.FindPayedOrderResponse;
import com.prgrms.nabmart.domain.order.service.response.CreateOrderResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.order.service.response.FindOrdersResponse;
import com.prgrms.nabmart.domain.order.service.response.UpdateOrderByCouponResponse;
import com.prgrms.nabmart.domain.user.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@Slf4j
public class OrderControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("findOrderByIdAndUserId 메서드 실행 시")
    class FindOrderByIdAndUserIdTest {

        @Test
        @DisplayName("성공")
        void findOrder() throws Exception {
            // given
            User user = user();
            Order order = completedOrder(1L, user);
            FindOrderDetailResponse findOrderDetailResponse = orderDetailResponse(order);

            when(orderService.findOrderByIdAndUserId(anyLong(), eq(order.getOrderId())))
                .thenReturn(findOrderDetailResponse);

            // when
            ResultActions result = mockMvc.perform(
                get("/api/v1/orders/{orderId}", order.getOrderId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isOk())
                .andDo(document("get-order",
                    pathParameters(
                        parameterWithName("orderId").description("주문 ID")
                    ),
                    responseFields(
                        fieldWithPath("createdAt").optional().type(STRING).description("주문 시각"),
                        fieldWithPath("totalPrice").type(NUMBER).description("최종 가격"),
                        fieldWithPath("status").type(STRING).description("주문 상태"),
                        fieldWithPath("orderItems[].itemId").optional().type(NUMBER)
                            .description("상품 ID"),
                        fieldWithPath("orderItems[].name").type(STRING).description("상품명"),
                        fieldWithPath("orderItems[].quantity").type(NUMBER).description("상품 수량"),
                        fieldWithPath("orderItems[].price").type(NUMBER).description("상품 가격")
                    )));
        }
    }

    @Nested
    @DisplayName("findOrders 메서드 실행 시")
    class FindOrdersTest {

        @Test
        @DisplayName("성공")
        void findOrders() throws Exception {
            // given
            User user = user();
            Order order = completedOrder(1L, user);
            FindOrdersResponse findOrdersResponse = FindOrdersResponse.of(List.of(order), 1);

            when(orderService.findOrders(any(), eq(1))).thenReturn(findOrdersResponse);

            // when
            ResultActions result = mockMvc.perform(
                get("/api/v1/orders?page={page}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isOk())
                .andDo(document("get-orders",
                    queryParameters(
                        parameterWithName("page").description("페이지 번호")
                    ),
                    responseFields(
                        fieldWithPath("totalPages").type(NUMBER).description("총 페이지 수"),
                        fieldWithPath("orders[].orderId").optional().type(NUMBER)
                            .description("주문 ID"),
                        fieldWithPath("orders[].name").optional().type(STRING)
                            .description("주문명"),
                        fieldWithPath("orders[].createdAt").optional().type(STRING)
                            .description("주문 시각"),
                        fieldWithPath("orders[].totalPrice").type(NUMBER).description("최종 가격"),
                        fieldWithPath("orders[].status").type(STRING).description("주문 상태"),
                        fieldWithPath("orders[].items[].itemId").optional().type(NUMBER)
                            .description("상품 ID"),
                        fieldWithPath("orders[].items[].name").type(STRING).description("상품명"),
                        fieldWithPath("orders[].items[].quantity").type(NUMBER)
                            .description("상품 수량"),
                        fieldWithPath("orders[].items[].price").type(NUMBER)
                            .description("상품 가격")
                    )));
        }
    }

    @Nested
    @DisplayName("createOrder 메서드 실행 시")
    class CreateOrderTest {

        @Test
        @DisplayName("성공")
        void createOrder() throws Exception {
            // given
            User user = user();
            Order order = pendingOrder(1L, user);
            CreateOrderRequest createOrderRequest = createOrderRequest();
            CreateOrderResponse createOrderResponse = createOrderResponse(order);
            when(orderService.createOrder(any())).thenReturn(createOrderResponse);

            // when
            ResultActions result = mockMvc.perform(
                post("/api/v1/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrderRequest))
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("orderId").type(NUMBER).description("주문 아이디"),
                        fieldWithPath("name").type(STRING).description("주문명"),
                        fieldWithPath("totalPrice").type(NUMBER).description("최종 가격"),
                        fieldWithPath("address").type(STRING).description("기본 배송지"),
                        fieldWithPath("deliveryFee").type(NUMBER).description("배달비")
                    )));
        }
    }

    @Nested
    @DisplayName("updateOrderByCoupon 메서드 실행 시")
    class UpdateOrderByCoupon {

        @Test
        @DisplayName("성공")
        void createOrder() throws Exception {
            // given
            User user = user();
            UserCoupon userCoupon = userCoupon(user);
            Order order = pendingOrder(1L, user);
            UpdateOrderByCouponResponse updateOrderByCouponResponse = UpdateOrderByCouponResponse.of(
                order, userCoupon.getCoupon());
            when(orderService.updateOrderByCoupon(any())).thenReturn(updateOrderByCouponResponse);

            // when
            ResultActions result = mockMvc.perform(
                post("/api/v1/orders/{orderId}/apply-coupon", order.getOrderId())
                    .param("couponId", String.valueOf(1L))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("totalPrice").type(NUMBER).description("쿠폰 적용후 총 가격"),
                        fieldWithPath("discountPrice").type(NUMBER).description("쿠폰 할인 금액")
                    )));
        }
    }

    @Nested
    @DisplayName("deleteOrder 메서드 실행 시")
    class DeleteOrder {

        @Test
        @DisplayName("성공")
        void deleteOrder() throws Exception {
            // given
            User user = user();
            Order order = pendingOrder(1L, user);

            // when
            ResultActions result = mockMvc.perform(
                delete("/api/v1/orders/{orderId}", order.getOrderId())
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                ));
        }
    }

    @Nested
    @DisplayName("결제 완료된 주문 목록 조회 api 호출 시")
    class FindPayedOrdersTest {

        @Test
        @DisplayName("성공")
        void findPayedOrders() throws Exception {
            //given
            int page = 0;
            FindPayedOrdersRequest findPayedOrdersRequest = new FindPayedOrdersRequest(page);
            FindPayedOrderResponse findPayedOrderResponse
                = new FindPayedOrderResponse(1L, "비비고 왕교자 외 2개", 20000);
            FindPayedOrdersResponse findPayedOrdersResponse = new FindPayedOrdersResponse(
                List.of(findPayedOrderResponse), 0, 1);

            given(orderService.findPayedOrders(any())).willReturn(findPayedOrdersResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/orders/payed")
                .header(AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(findPayedOrdersRequest)));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("page").type(NUMBER).description("페이지 번호")
                    ),
                    responseFields(
                        fieldWithPath("orders").type(ARRAY).description("주문 목록"),
                        fieldWithPath("orders[].orderId").type(NUMBER).description("주문 ID"),
                        fieldWithPath("orders[].name").type(STRING).description("주문 이름"),
                        fieldWithPath("orders[].price").type(NUMBER).description("주문 가격"),
                        fieldWithPath("page").type(NUMBER).description("페이지 번호"),
                        fieldWithPath("totalElements").type(NUMBER).description("총 요소 개수")
                    )
                ));
        }
    }
}
