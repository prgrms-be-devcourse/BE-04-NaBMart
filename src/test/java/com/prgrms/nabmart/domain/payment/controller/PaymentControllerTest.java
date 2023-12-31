package com.prgrms.nabmart.domain.payment.controller;

import static com.prgrms.nabmart.domain.order.support.OrderFixture.pendingOrder;
import static com.prgrms.nabmart.domain.payment.support.PaymentDtoFixture.paymentRequestResponse;
import static com.prgrms.nabmart.domain.user.support.UserFixture.userWithUserId;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.order.Order;
import com.prgrms.nabmart.domain.payment.PaymentStatus;
import com.prgrms.nabmart.domain.payment.service.response.PaymentRequestResponse;
import com.prgrms.nabmart.domain.payment.service.response.PaymentResponse;
import com.prgrms.nabmart.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class PaymentControllerTest extends BaseControllerTest {

    @Value("${payment.toss.success-url}")
    private String successCallBackUrl;

    @Value("${payment.toss.fail-url}")
    private String failCallBackUrl;

    @Nested
    @DisplayName("pay 메서드 실행 시")
    class payTest {

        @Test
        @DisplayName("성공")
        void pay() throws Exception {
            // given
            User user = userWithUserId();
            Order order = pendingOrder(1, user);
            PaymentRequestResponse paymentResponse = paymentRequestResponse(
                order,
                successCallBackUrl,
                failCallBackUrl
            );

            when(paymentService.pay(order.getOrderId(), user.getUserId())).thenReturn(
                paymentResponse);

            // when
            ResultActions result = mockMvc.perform(
                post("/api/v1/pays/{orderId}", order.getOrderId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                    pathParameters(
                        parameterWithName("orderId").description("주문 ID")
                    ),
                    responseFields(
                        fieldWithPath("amount").type(NUMBER).description("금액"),
                        fieldWithPath("orderId").type(STRING).description("주문 UUID"),
                        fieldWithPath("orderName").type(STRING).description("주문 대표명"),
                        fieldWithPath("customerEmail").type(STRING).description("고객 이메일"),
                        fieldWithPath("customerName").type(STRING).description("고객명"),
                        fieldWithPath("successUrl").type(STRING).description("성공 시 콜백 url"),
                        fieldWithPath("failUrl").type(STRING).description("실패 시 콜백 url")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("paySuccess 메서드 실행 시")
    class paySuccessTest {

        @Test
        @DisplayName("성공")
        void paySuccess() throws Exception {
            // given
            User user = userWithUserId();
            Order order = pendingOrder(1, user);

            String paymentKey = "paymentKey";

            when(paymentService.processSuccessPayment(user.getUserId(), order.getUuid(), paymentKey,
                order.getPrice()))
                .thenReturn(new PaymentResponse(PaymentStatus.SUCCESS.toString(), null));

            // when
            ResultActions result = mockMvc.perform(
                get("/api/v1/pays/toss/success")
                    .queryParam("orderId", order.getUuid())
                    .queryParam("paymentKey", paymentKey)
                    .queryParam("amount", String.valueOf(order.getPrice()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isOk())
                .andDo(restDocs.document(queryParameters(
                        parameterWithName("orderId").description("주문 ID"),
                        parameterWithName("paymentKey").description("결제 키"),
                        parameterWithName("amount").description("금액")
                    ),
                    responseFields(
                        fieldWithPath("status").type(STRING).description("성공 여부"),
                        fieldWithPath("message").type(STRING).description("에러 메시지").optional()
                    )
                ));
        }
    }

    @Nested
    @DisplayName("payFail 메서드 실행 시")
    class payFailTest {

        @Test
        @DisplayName("성공")
        void payFail() throws Exception {
            // given
            User user = userWithUserId();
            Order order = pendingOrder(1, user);
            String errorMessage = "errorMessage";

            when(paymentService.processFailPayment(user.getUserId(), order.getUuid(), errorMessage))
                .thenReturn(new PaymentResponse(PaymentStatus.FAILED.toString(), errorMessage));

            // when
            ResultActions result = mockMvc.perform(
                get("/api/v1/pays/toss/fail")
                    .queryParam("orderId", order.getUuid())
                    .queryParam("message", errorMessage)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, accessToken));

            // then
            result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("orderId").description("주문 ID"),
                        parameterWithName("message").description("에러 메시지")
                    ),
                    responseFields(
                        fieldWithPath("status").type(STRING).description("성공 여부"),
                        fieldWithPath("message").type(STRING).description("에러 메시지")
                    )
                ));
        }
    }
}
