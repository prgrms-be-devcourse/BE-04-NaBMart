package com.prgrms.nabmart.domain.item.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.item.domain.ItemSortType;
import com.prgrms.nabmart.domain.item.service.request.FindNewItemsCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse.FindItemResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse.PageInfoResponse;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ItemControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("신상품 조회 api 호출 시")
    class FindNewItemsApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            FindNewItemsCommand command = FindNewItemsCommand.of(0, 3, ItemSortType.LOWEST_AMOUNT);
            FindItemsResponse response = FindItemsResponse.of(
                new PageInfoResponse(0, 1, 2L),
                Arrays.asList(
                    new FindItemResponse(1L, "name 1", 3000, 50, 3, 10, 3.5),
                    new FindItemResponse(2L, "name 2", 30000, 500, 3, 10, 3.5)
                )
            );

            given(itemService.findNewItems(command)).willReturn(response);

            // When
            ResultActions resultActions = mockMvc.perform(get("/api/v1/items/new")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("pageSize", "3")
                .param("sort", "LOWEST_AMOUNT"));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find New Items",
                    responseFields(
                        fieldWithPath("pageInfo.currentPage").type(JsonFieldType.NUMBER)
                            .description("현재 페이지"),
                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER)
                            .description("총 페이지 수"),
                        fieldWithPath("pageInfo.totalItems").type(JsonFieldType.NUMBER)
                            .description("총 상품 수"),
                        fieldWithPath("items[]").type(JsonFieldType.ARRAY)
                            .description("상품 리스트"),
                        fieldWithPath("items[].itemId").type(JsonFieldType.NUMBER)
                            .description("상품 ID"),
                        fieldWithPath("items[].name").type(JsonFieldType.STRING)
                            .description("상품 이름"),
                        fieldWithPath("items[].price").type(JsonFieldType.NUMBER)
                            .description("상품 가격"),
                        fieldWithPath("items[].discount").type(JsonFieldType.NUMBER)
                            .description("상품 할인"),
                        fieldWithPath("items[].reviewCount").type(JsonFieldType.NUMBER)
                            .description("리뷰 수"),
                        fieldWithPath("items[].like").type(JsonFieldType.NUMBER)
                            .description("좋아요 수"),
                        fieldWithPath("items[].rate").type(JsonFieldType.NUMBER)
                            .description("평균 평점")
                    )
                ));
        }
    }
}
