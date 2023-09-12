package com.prgrms.nabmart.domain.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.delivery.controller.request.StartDeliveryRequest;
import com.prgrms.nabmart.domain.delivery.service.response.FindDeliveryDetailResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindRiderDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindRiderDeliveriesResponse.FindRiderDeliveryResponse;
import com.prgrms.nabmart.domain.delivery.service.response.FindWaitingDeliveriesResponse;
import com.prgrms.nabmart.domain.delivery.support.DeliveryFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
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
                        fieldWithPath("orderId").type(NUMBER).description("주문 ID"),
                        fieldWithPath("name").type(STRING).description("주문 이름"),
                        fieldWithPath("price").type(NUMBER).description("주문 가격")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("배달상태 갱신 - 배차 요청 API 호출 시")
    class AcceptDeliveryTest {

        @Test
        @DisplayName("성공")
        void acceptDelivery() throws Exception {
            //given
            Long deliveryId = 1L;

            //when
            ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/deliveries/{deliveryId}/accept", deliveryId)
                    .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 ID")
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
            ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/deliveries/{deliveryId}/pickup", deliveryId)
                    .header(AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(startDeliveryRequest)));

            //then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
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
                .perform(patch("/api/v1/deliveries/{deliveryId}/complete", deliveryId)
                    .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 ID")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("배달 대기 상태의 배달 목록 조회 호출 시")
    class FindWaitingDeliveriesTest {

        @Test
        @DisplayName("성공")
        void findWaitingDeliveries() throws Exception {
            //given
            int page = 0;
            int size = 10;
            FindWaitingDeliveriesResponse deliveriesResponse = DeliveryFixture.findDeliveriesResponse();

            given(deliveryService.findWaitingDeliveries(any())).willReturn(deliveriesResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/deliveries/waiting")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지"),
                        parameterWithName("size").description("사이즈")
                    ),
                    responseFields(
                        fieldWithPath("deliveries").type(ARRAY).description("배달 목록"),
                        fieldWithPath("deliveries[].deliveryId").type(NUMBER).description("배달 ID"),
                        fieldWithPath("page").type(NUMBER).description("페이지"),
                        fieldWithPath("totalElements").type(NUMBER).description("사이즈")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("라이더가 배차 받은 배달 목록 조회 API 호출 시")
    class FindRiderDeliveriesTest {

        @Test
        @DisplayName("성공")
        void findRidersDeliveries() throws Exception {
            //given
            DeliveryStatus deliveryStatus = DeliveryStatus.ACCEPTING_ORDER;
            FindRiderDeliveryResponse findRiderDeliveryResponse = new FindRiderDeliveryResponse(
                1L,
                deliveryStatus,
                LocalDateTime.now().plusMinutes(20),
                "address",
                3000
            );
            FindRiderDeliveriesResponse findRiderDeliveriesResponse
                = new FindRiderDeliveriesResponse(
                    List.of(findRiderDeliveryResponse),
                0,
                1);

            given(deliveryService.findRiderDeliveries(any()))
                .willReturn(findRiderDeliveriesResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/deliveries")
                .queryParam("deliveryStatus", deliveryStatus.name())
                .queryParam("page", "0")
                .queryParam("size", "10")
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    queryParameters(
                        parameterWithName("deliveryStatus").description("배달 상태"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("size").description("페이지 사이즈")
                    )
                ));
        }
    }
}
