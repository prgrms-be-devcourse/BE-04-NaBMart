package com.prgrms.nabmart.domain.event.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.event.controller.request.RegisterEventItemsRequest;
import com.prgrms.nabmart.domain.event.controller.request.RegisterEventRequest;
import com.prgrms.nabmart.domain.event.service.response.FindEventDetailResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventDetailResponse.EventDetailResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventDetailResponse.EventItemResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse.FindEventResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class EventControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("이벤트 등록하는 api 호출 시")
    class RegisterEventApi {

        @Test
        @DisplayName("성공")
        public void registerEvent() throws Exception {
            // Given
            RegisterEventRequest request = new RegisterEventRequest("TestTitle", "TestDescription");
            String requestBody = objectMapper.writeValueAsString(request);

            given(eventService.registerEvent(any())).willReturn(1L);

            // When
            ResultActions resultActions = mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

            // Then
            resultActions.andExpect(status().isCreated())
                .andDo(document("Register Event"
                ));
        }
    }

    @Nested
    @DisplayName("이벤트 전체 조회하는 api 호출 시")
    class FindEventsApi {

        @Test
        @DisplayName("성공")
        public void findEvents() throws Exception {
            // Given
            FindEventsResponse eventResponses = FindEventsResponse.of(List.of(
                new FindEventResponse(1L, "Event 1", "Description 1"),
                new FindEventResponse(2L, "Event 2", "Description 2")
            ));

            given(eventService.findEvents()).willReturn(eventResponses);

            // When
            ResultActions resultActions = mockMvc.perform(
                get("/api/v1/events").accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find Events",
                    responseFields(
                        fieldWithPath("events[].eventId").type(JsonFieldType.NUMBER)
                            .description("이벤트 ID"),
                        fieldWithPath("events[].name").type(JsonFieldType.STRING)
                            .description("이벤트 이름"),
                        fieldWithPath("events[].description").type(JsonFieldType.STRING)
                            .description("이벤트 설명")

                    )
                ));
        }
    }

    @Nested
    @DisplayName("이벤트 디테일 조회하는 api 호출 시")
    class FindEventDetailApi {

        @Test
        @DisplayName("성공")
        public void findEventDetail() throws Exception {
            // Given
            FindEventDetailResponse eventDetailResponse = FindEventDetailResponse.of(
                new EventDetailResponse(
                    1L, "Event Title", "Event Description"
                ),
                List.of(
                    new EventItemResponse(
                        1L, "name 1", 3000, 1000, 5, 3, 4.5
                    ),
                    new EventItemResponse(
                        2L, "name 2", 50000, 2000, 10, 7, 4
                    )
                )
            );

            given(eventService.findEventDetail(any())).willReturn(eventDetailResponse);

            // When
            ResultActions resultActions = mockMvc.perform(
                get("/api/v1/events/1").accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find Event Detail",
                        responseFields(
                            fieldWithPath("event.eventId").type(JsonFieldType.NUMBER)
                                .description("이벤트 ID"),
                            fieldWithPath("event.eventTitle").type(JsonFieldType.STRING)
                                .description("이벤트 제목"),
                            fieldWithPath("event.eventDescription").type(JsonFieldType.STRING)
                                .description("이벤트 설명"),
                            fieldWithPath("items[].itemId").type(JsonFieldType.NUMBER)
                                .description("아이템 ID"),
                            fieldWithPath("items[].name").type(JsonFieldType.STRING)
                                .description("아이템 이름"),
                            fieldWithPath("items[].price").type(JsonFieldType.NUMBER)
                                .description("아이템 가격"),
                            fieldWithPath("items[].discount").type(JsonFieldType.NUMBER)
                                .description("아이템 할인"),
                            fieldWithPath("items[].reviewCount").type(JsonFieldType.NUMBER)
                                .description("리뷰 개수"),
                            fieldWithPath("items[].like").type(JsonFieldType.NUMBER)
                                .description("좋아요 수"),
                            fieldWithPath("items[].rate").type(JsonFieldType.NUMBER)
                                .description("평균 평점")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("이벤트에 상품을 등록하는 api 호출 시")
    class RegisterEventItemsApi {

        @Test
        @DisplayName("성공")
        public void registerEventItems() throws Exception {
            // Given
            RegisterEventItemsRequest request = new RegisterEventItemsRequest(
                Arrays.asList(1L, 2L));
            String requestBody = objectMapper.writeValueAsString(request);

            given(eventItemService.registerEventItems(any())).willReturn(1L);

            // When
            ResultActions resultActions = mockMvc.perform(post("/api/v1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

            // Then
            resultActions.andExpect(status().isCreated())
                .andDo(document("Register Event Items"
                ));
        }
    }
}
