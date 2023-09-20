package com.prgrms.nabmart.domain.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import com.prgrms.nabmart.domain.delivery.DeliveryStatus;
import com.prgrms.nabmart.domain.delivery.controller.request.RegisterDeliveryRequest;
import com.prgrms.nabmart.domain.delivery.controller.request.StartDeliveryRequest;
import com.prgrms.nabmart.domain.delivery.exception.AlreadyAssignedDeliveryException;
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
    @DisplayName("배달 생성 API 호출 시")
    class RegisterDeliveryTest {

        @Test
        @DisplayName("성공")
        void registerDelivery() throws Exception {
            //given
            RegisterDeliveryRequest registerDeliveryRequest
                = new RegisterDeliveryRequest(60);

            given(deliveryService.registerDelivery(any())).willReturn(1L);

            //when
            ResultActions resultActions = mockMvc.perform(
                post("/api/v1/orders/{orderId}/deliveries", 1L)
                .header(AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDeliveryRequest)));

            //then
            resultActions.andExpect(status().isCreated())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("orderId").description("주문 ID")
                    ),
                    requestFields(
                        fieldWithPath("estimateMinutes").type(NUMBER).description("배달 예상 시간(분)")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("생성된 리소스 위치")
                    )
                ));
        }
    }

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
                .perform(get("/api/v1/orders/{orderId}/deliveries", orderId)
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
                        fieldWithPath("deliveryStatus").type(STRING).description("배달 상태"),
                        fieldWithPath("createdAt").type(STRING).description("배달 생성 시각"),
                        fieldWithPath("arrivedAt").type(STRING).description("배달 완료 예정 시각"),
                        fieldWithPath("orderId").type(NUMBER).description("주문 ID"),
                        fieldWithPath("orderName").type(STRING).description("주문 이름"),
                        fieldWithPath("orderPrice").type(NUMBER).description("주문 가격"),
                        fieldWithPath("riderRequest").type(STRING).description("배달원 요청 사항")
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

        @Test
        @DisplayName("예외: 이미 배정된 배달")
        void exceptionWhenAlreadyAssignedDelivery() throws Exception {
            //given
            doThrow(AlreadyAssignedDeliveryException.class).when(deliveryService)
                .acceptDelivery(any());

            //when
            ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/deliveries/{deliveryId}/accept", 1L)
                    .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isConflict());
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
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지"),
                        parameterWithName("size").description("사이즈")
                    ),
                    responseFields(
                        fieldWithPath("deliveries").type(ARRAY).description("배달 목록"),
                        fieldWithPath("deliveries[].deliveryId").type(NUMBER)
                            .description("배달 ID"),
                        fieldWithPath("deliveries[].arrivedAt").type(STRING)
                            .description("배달 완료 예정 시간"),
                        fieldWithPath("deliveries[].createdAt").type(STRING)
                            .description("배달 접수 시간"),
                        fieldWithPath("deliveries[].address").type(STRING)
                            .description("배달 목적지"),
                        fieldWithPath("deliveries[].deliveryFee").type(NUMBER)
                            .description("배달비"),
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
        void findRiderDeliveries() throws Exception {
            //given
            DeliveryStatus deliveryStatus = DeliveryStatus.ACCEPTING_ORDER;
            FindRiderDeliveryResponse findRiderDeliveryResponse = new FindRiderDeliveryResponse(
                1L,
                deliveryStatus,
                LocalDateTime.now().plusMinutes(20),
                LocalDateTime.now(),
                "address",
                15000,
                "문 앞에 두고 벨 눌러주세요.",
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
                .queryParam("deliveryStatuses", deliveryStatus.name())
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
                        parameterWithName("deliveryStatuses").description("배달 상태 목록"),
                        parameterWithName("page").description("페이지"),
                        parameterWithName("size").description("페이지 사이즈")
                    ),
                    responseFields(
                        fieldWithPath("deliveries").type(ARRAY).description("배달 목록"),
                        fieldWithPath("deliveries[].deliveryId").type(NUMBER)
                            .description("배달 ID"),
                        fieldWithPath("deliveries[].deliveryStatus").type(STRING)
                            .description("배달 상태"),
                        fieldWithPath("deliveries[].arrivedAt").type(STRING)
                            .description("배달 완료 예정 시각"),
                        fieldWithPath("deliveries[].createdAt").type(STRING)
                            .description("배달 생성 시각"),
                        fieldWithPath("deliveries[].address").type(STRING)
                            .description("배달 목적지"),
                        fieldWithPath("deliveries[].orderPrice").type(NUMBER)
                            .description("주문 가격"),
                        fieldWithPath("deliveries[].riderRequest").type(STRING)
                            .description("배달원 요청 사항"),
                        fieldWithPath("deliveries[].deliveryFee").type(NUMBER)
                            .description("배달비"),
                        fieldWithPath("page").type(NUMBER).description("페이지"),
                        fieldWithPath("totalElements").type(NUMBER).description("총 요소 갯수")
                    )
                ));
        }
    }
}
