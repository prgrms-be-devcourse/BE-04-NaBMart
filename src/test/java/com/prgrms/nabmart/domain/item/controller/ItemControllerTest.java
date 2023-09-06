package com.prgrms.nabmart.domain.item.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ItemControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("findItemsByMainCategory 메서드 호출 시")
    class FindItemsByMainCategory {

        FindItemsResponse findItemsResponse = ItemFixture.findItemsResponse();

        @Test
        @DisplayName("성공")
        void findItemsByMainCategory() throws Exception {
            // Given
            Long previousItemId = 5L;
            int size = 3;
            String main = "mainCategory";
            when(itemService.findItemsByMainCategory(previousItemId, main, size)).thenReturn(
                findItemsResponse);

            // Then&When
            mockMvc.perform(get("/api/v1/items")
                    .queryParam("previousItemId", "5")
                    .queryParam("size", "3")
                    .queryParam("main", main))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("previousItemId").description("마지막에 조회한 아이템 Id"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("main").description("대카테고리명")
                    )
                ));

        }

    }

    @Nested
    @DisplayName("상품 디테일 조회하는 api 호출 시")
    class FindItemDetailApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            Long itemId = 1L;
            FindItemDetailResponse response = FindItemDetailResponse.of(
                itemId, "name", 3000, "description", 10, 4.5, 10, 300, 30, 5
            );

            given(itemService.findItemDetail(any())).willReturn(response);

            // When
            ResultActions resultActions = mockMvc.perform(
                get("/api/v1/items/{itemId}", itemId).accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find Item Detail",
                    pathParameters(
                        parameterWithName("itemId").description("상품 ID")
                    ),
                    responseFields(
                        fieldWithPath("itemId").type(JsonFieldType.NUMBER)
                            .description("아이템 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING)
                            .description("상품 이름"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER)
                            .description("상품 가격"),
                        fieldWithPath("description").type(JsonFieldType.STRING)
                            .description("상품 설명."),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                            .description("상품 수량"),
                        fieldWithPath("rate").type(JsonFieldType.NUMBER)
                            .description("상품 평점"),
                        fieldWithPath("reviewCount").type(JsonFieldType.NUMBER)
                            .description("리뷰 수"),
                        fieldWithPath("discount").type(JsonFieldType.NUMBER)
                            .description("할인"),
                        fieldWithPath("like").type(JsonFieldType.NUMBER)
                            .description("좋아요 수."),
                        fieldWithPath("maxBuyQuantity").type(JsonFieldType.NUMBER)
                            .description("최대 구매 수량")
                    )
                ));

        }
    }
}
