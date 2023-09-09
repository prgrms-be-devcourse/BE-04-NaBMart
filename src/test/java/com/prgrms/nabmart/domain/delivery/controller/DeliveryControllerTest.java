package com.prgrms.nabmart.domain.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.delivery.controller.request.StartDeliveryRequest;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.delivery.support.DeliveryFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

class DeliveryControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("배달 현황 조회 API 호출 시")
    class FindDeliveryTest {

        @Test
        @DisplayName("성공")
        void findDelivery() throws Exception {
            //given
            Long orderId = 1L;
            FindDeliveryDetailResponse findDeliveryDetailResponse
                = DeliveryFixture.findDeliveryDetailResponse();

            given(deliveryService.findDelivery(any())).willReturn(findDeliveryDetailResponse);

            //when
            ResultActions resultActions = mockMvc
                .perform(get("/api/v1/deliveries/{orderId}", orderId)
                    .header(AUTHORIZATION, accessToken)
                    .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("orderId").description("주문 ID")
                    ),
                    responseFields(
                        fieldWithPath("deliveryId").type(NUMBER).description("배달 ID"),
                        fieldWithPath("deliveryStatus").type(STRING).description("배달 ID"),
                        fieldWithPath("arrivedAt").type(STRING).description("도착 시간"),
                        fieldWithPath("address").type(STRING).description("주소"),
                        fieldWithPath("orderId").type(NUMBER).description("주문 ID"),
                        fieldWithPath("name").type(STRING).description("주문 이름"),
                        fieldWithPath("price").type(NUMBER).description("주문 가격")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("배달상태 갱신 - 배달시작 API 호출 시")
    class StartDeliveryTest {

        @Test
        @DisplayName("성공")
        void startDelivery() throws Exception {
            //given
            Long deliveryId = 1L;
            int deliveryEstimateMinutes = 20;
            StartDeliveryRequest startDeliveryRequest
                = new StartDeliveryRequest(deliveryEstimateMinutes);

            //when
            ResultActions resultActions
                = mockMvc.perform(patch("/api/v1/deliveries/pickup/{deliveryId}", deliveryId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(startDeliveryRequest)));

            //then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 ID")
                    ),
                    requestFields(
                        fieldWithPath("deliveryEstimateMinutes").type(NUMBER)
                            .description("배달 예상 소요 시간(분)")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("배달 상태 갱신 - 배달완료 API 호출 시")
    class CompleteDeliveryTest {

        @Test
        @DisplayName("성공")
        void completeDelivery() throws Exception {
            //given
            Long deliveryId = 1L;

            //when
            ResultActions resultActions = mockMvc
                .perform(patch("/api/v1/deliveries/complete/{deliveryId}", deliveryId));

            //then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 ID")
                    )
                ));
        }
    }
}
