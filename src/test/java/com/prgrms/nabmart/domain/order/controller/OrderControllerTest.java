package com.prgrms.nabmart.domain.order.controller;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.completedOrder;
import static com.prgrms.nabmart.domain.order.support.OrderFixture.orderDetailResponse;
import static com.prgrms.nabmart.domain.user.support.UserFixture.user;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.order.service.response.FindOrderDetailResponse;
import com.prgrms.nabmart.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@WebMvcTest(OrderController.class)
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
                    .contentType(MediaType.APPLICATION_JSON));

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

}
