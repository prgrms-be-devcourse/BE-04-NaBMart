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
import com.prgrms.nabmart.domain.item.service.request.FindItemsByCategoryCommand;
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
    @DisplayName("findItemsByCategory 메서드 호출 시")
    class FindItemsByMainCategory {

        FindItemsResponse findItemsResponse = ItemFixture.findItemsResponse();
        String mainCategoryName = "main category";
        String subCategoryName = "sub category";
        FindItemsByCategoryCommand findItemsByMainCategoryCommand = ItemFixture.findItemsByCategoryCommand(
            mainCategoryName, null);
        FindItemsByCategoryCommand findItemsBySubCategoryCommand = ItemFixture.findItemsByCategoryCommand(
            mainCategoryName, subCategoryName);

        @Test
        @DisplayName("성공: 대카테고리 전체품목 조회")
        void findItemsByMainCategory() throws Exception {
            // Given
            when(itemService.findItemsByCategory(findItemsByMainCategoryCommand)).thenReturn(
                findItemsResponse);

            // Then&When
            mockMvc.perform(get("/api/v1/items")
                    .queryParam("lastIdx", "5")
                    .queryParam("size", "3")
                    .queryParam("main", mainCategoryName)
                    .queryParam("sort", "DISCOUNT"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("lastIdx").description("마지막에 조회한 아이템 Id"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("main").description("대카테고리명"),
                        parameterWithName("sort").description("정렬 기준명")
                    )
                ));
        }

        @Test
        @DisplayName("성공: 소카테고리 전체품목 조회")
        void findItemsBySubCategory() throws Exception {
            // Given
            when(itemService.findItemsByCategory(findItemsBySubCategoryCommand)).thenReturn(
                findItemsResponse);

            // Then&When
            mockMvc.perform(get("/api/v1/items")
                    .queryParam("lastIdx", "5")
                    .queryParam("size", "3")
                    .queryParam("main", mainCategoryName)
                    .queryParam("sub", subCategoryName)
                    .queryParam("sort", "DISCOUNT"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("lastIdx").description("마지막에 조회한 아이템 Id"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("main").description("대카테고리명"),
                        parameterWithName("sub").description("대카테고리에 속한 소카테고리명"),
                        parameterWithName("sort").description("정렬 기준명")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("상품 디테일 조회하는 api 호출 시")
    class FindItemDetailApi {

        @Test
        @DisplayName("성공")
        public void findItemDetail() throws Exception {
            // Given
            Long itemId = 1L;
            FindItemDetailResponse response = FindItemDetailResponse.of(
                itemId, "name", 3000, "description", 10, 4.5,
                10, 300, 30, 5
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

    @Nested
    @DisplayName("신상품 조회하는 api 호출 시")
    class FindNewItemsApi {

        FindItemsResponse findItemsResponse = ItemFixture.findItemsResponse();

        @Test
        @DisplayName("성공")
        public void findNewItems() throws Exception {
            // Given
            given(itemService.findNewItems(any())).willReturn(findItemsResponse);

            // When
            ResultActions resultActions = mockMvc.perform(
                get("/api/v1/items/new")
                    .queryParam("lastIdx", "1")
                    .queryParam("size", "5")
                    .queryParam("sort", "NEW")
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find New Items",
                    queryParameters(
                        parameterWithName("lastIdx").description("마지막에 조회한 아이템의 특성값"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("sort").description("정렬 기준명")
                    ),
                    responseFields(
                        fieldWithPath("items").type(JsonFieldType.ARRAY)
                            .description("List of items"),
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
                            .description("평점")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("인기 상품 조회하는 api 호출 시")
    class FindHotItemsApi {

        FindItemsResponse findItemsResponse = ItemFixture.findItemsResponse();

        @Test
        @DisplayName("성공")
        public void findHotItems() throws Exception {
            // Given
            given(itemService.findHotItems(any())).willReturn(findItemsResponse);

            // When
            ResultActions resultActions = mockMvc.perform(
                get("/api/v1/items/hot")
                    .queryParam("lastIdx", "-1")
                    .queryParam("size", "3")
                    .queryParam("sort", "NEW")
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find Hot Items",
                    queryParameters(
                        parameterWithName("lastIdx").description("마지막에 조회한 아이템의 특성값"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("sort").description("정렬 기준명")
                    ),
                    responseFields(
                        fieldWithPath("items").type(JsonFieldType.ARRAY)
                            .description("List of items"),
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
                            .description("평점")
                    )
                ));
        }
    }
}
