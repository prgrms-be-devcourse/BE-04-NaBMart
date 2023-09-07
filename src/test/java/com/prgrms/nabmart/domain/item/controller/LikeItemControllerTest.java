package com.prgrms.nabmart.domain.item.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.item.controller.request.RegisterLikeItemRequest;
import com.prgrms.nabmart.domain.item.service.response.FindLikeItemsResponse;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class LikeItemControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("찜 아이템 등록 API 호출 시")
    class RegisterLikeItemTest {

        @Test
        @DisplayName("성공")
        void registerLikeItem() throws Exception {
            //given
            RegisterLikeItemRequest registerLikeItemRequest = ItemFixture.registerLikeItemRequest();

            given(likeItemService.registerLikeItem(any())).willReturn(1L);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerLikeItemRequest))
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isCreated())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("itemId").type(NUMBER).description("아이템 ID")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("리소스 접근 가능 경로")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("찜 아이템 삭제 API 호출 시")
    class DeleteLikeItemTest {

        @Test
        @DisplayName("성공")
        void deleteLikeItem() throws Exception {
            //given
            Long likeItemId = 1L;

            //when
            ResultActions resultActions = mockMvc.perform(delete(
                "/api/v1/likes/{likeItemId}", likeItemId)
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("likeItemId").description("찜 상품 ID")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("찜 아이템 목록 조회 API 호출 시")
    class FindLikeItemsTest {

        @Test
        @DisplayName("성공")
        void findLikeItems() throws Exception {
            //given
            int page = 0;
            int size = 10;
            FindLikeItemsResponse findLikeItemsResponse = ItemFixture.findLikeItemsResponse();

            given(likeItemService.findLikeItems(any())).willReturn(findLikeItemsResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/likes")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .header(AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지"),
                        parameterWithName("size").description("페이지 사이즈")
                    ),
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    responseFields(
                        fieldWithPath("items").type(ARRAY).description("찜 상품 목록"),
                        fieldWithPath("items[].likeItemId").type(NUMBER).description("찜 상품 ID"),
                        fieldWithPath("items[].itemId").type(NUMBER).description("상품 ID"),
                        fieldWithPath("items[].name").type(STRING).description("상품 이름"),
                        fieldWithPath("items[].price").type(NUMBER).description("상품 가격"),
                        fieldWithPath("items[].discount").type(NUMBER).description("상품 할인 금액"),
                        fieldWithPath("items[].reviewCount").type(NUMBER).description("리뷰 수"),
                        fieldWithPath("items[].like").type(NUMBER).description("찜 수"),
                        fieldWithPath("items[].rate").type(NUMBER).description("평점"),
                        fieldWithPath("page").type(NUMBER).description("페이지"),
                        fieldWithPath("totalElements").type(NUMBER).description("총 갯수")
                    )
                ));
        }
    }
}