package com.prgrms.nabmart.domain.item.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
import com.prgrms.nabmart.domain.item.controller.request.RegisterItemRequest;
import com.prgrms.nabmart.domain.item.controller.request.UpdateItemRequest;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByCategoryCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemDetailResponse;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.service.response.FindNewItemsResponse;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
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
                        fieldWithPath("itemId").type(NUMBER)
                            .description("아이템 ID"),
                        fieldWithPath("name").type(STRING)
                            .description("상품 이름"),
                        fieldWithPath("price").type(NUMBER)
                            .description("상품 가격"),
                        fieldWithPath("description").type(STRING)
                            .description("상품 설명."),
                        fieldWithPath("quantity").type(NUMBER)
                            .description("상품 수량"),
                        fieldWithPath("rate").type(NUMBER)
                            .description("상품 평점"),
                        fieldWithPath("reviewCount").type(NUMBER)
                            .description("리뷰 수"),
                        fieldWithPath("discount").type(NUMBER)
                            .description("할인"),
                        fieldWithPath("like").type(NUMBER)
                            .description("좋아요 수."),
                        fieldWithPath("maxBuyQuantity").type(NUMBER)
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
                    .queryParam("lastItemId", "1")
                    .queryParam("size", "5")
                    .queryParam("sort", "NEW")
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find New Items",
                    queryParameters(
                        parameterWithName("lastIdx").description("마지막에 조회한 아이템의 특성값"),
                        parameterWithName("lastItemId").description("마지막에 조회한 아이템 ID"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("sort").description("정렬 기준명")
                    ),
                    responseFields(
                        fieldWithPath("items").type(ARRAY)
                            .description("List of items"),
                        fieldWithPath("items[].itemId").type(NUMBER)
                            .description("상품 ID"),
                        fieldWithPath("items[].name").type(STRING)
                            .description("상품 이름"),
                        fieldWithPath("items[].price").type(NUMBER)
                            .description("상품 가격"),
                        fieldWithPath("items[].discount").type(NUMBER)
                            .description("상품 할인"),
                        fieldWithPath("items[].reviewCount").type(NUMBER)
                            .description("리뷰 수"),
                        fieldWithPath("items[].like").type(NUMBER)
                            .description("좋아요 수"),
                        fieldWithPath("items[].rate").type(NUMBER)
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
                    .queryParam("lastItemId", "-1")
                    .queryParam("size", "3")
                    .queryParam("sort", "NEW")
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find Hot Items",
                    queryParameters(
                        parameterWithName("lastIdx").description("마지막에 조회한 아이템의 특성값"),
                        parameterWithName("lastItemId").description("마지막에 조회한 아이템 ID"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("sort").description("정렬 기준명")
                    ),
                    responseFields(
                        fieldWithPath("items").type(ARRAY)
                            .description("List of items"),
                        fieldWithPath("items[].itemId").type(NUMBER)
                            .description("상품 ID"),
                        fieldWithPath("items[].name").type(STRING)
                            .description("상품 이름"),
                        fieldWithPath("items[].price").type(NUMBER)
                            .description("상품 가격"),
                        fieldWithPath("items[].discount").type(NUMBER)
                            .description("상품 할인"),
                        fieldWithPath("items[].reviewCount").type(NUMBER)
                            .description("리뷰 수"),
                        fieldWithPath("items[].like").type(NUMBER)
                            .description("좋아요 수"),
                        fieldWithPath("items[].rate").type(NUMBER)
                            .description("평점")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("상품 수정 api 호출 시")
    class UpdateItem {

        UpdateItemRequest updateItemRequest = ItemFixture.updateItemRequest();
        Long ITEM_ID = 1L;

        @Test
        @DisplayName("성공")
        public void updateItem() throws Exception {

            // When
            ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/items/{itemId}", ITEM_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateItemRequest)));

            // Then
            resultActions.andExpect(status().isNoContent())
                .andDo(document(
                    "Update Item",
                    requestFields(
                        fieldWithPath("name").type(STRING)
                            .description("상품명"),
                        fieldWithPath("price").type(NUMBER)
                            .description("상품 단가"),
                        fieldWithPath("quantity").type(NUMBER)
                            .description("상품 재고수량"),
                        fieldWithPath("discount").type(NUMBER)
                            .description("상품 할인율"),
                        fieldWithPath("maxBuyQuantity").type(NUMBER)
                            .description("상품 최대 주문수량"),
                        fieldWithPath("description").type(STRING)
                            .description("상품 설명"),
                        fieldWithPath("mainCategoryId").type(NUMBER)
                            .description("상품 대카테고리 ID"),
                        fieldWithPath("subCategoryId").type(NUMBER)
                            .description("상품 소카테고리 ID")
                    )
                ));
        }


    }

    @Nested
    @DisplayName("상품 등록 api 호출 시")
    class SaveItem {

        RegisterItemRequest registerItemRequest = ItemFixture.registerItemRequest();

        @Test
        @DisplayName("성공")
        public void registerItem() throws Exception {

            // When
            ResultActions resultActions = mockMvc.perform(
                post("/api/v1/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerItemRequest)));

            // Then
            resultActions.andExpect(status().isCreated())
                .andDo(document(
                        "Register Item",
                        requestFields(
                            fieldWithPath("name").type(STRING)
                                .description("상품명"),
                            fieldWithPath("price").type(NUMBER)
                                .description("상품 단가"),
                            fieldWithPath("quantity").type(NUMBER)
                                .description("상품 재고수량"),
                            fieldWithPath("discount").type(NUMBER)
                                .description("상품 할인율"),
                            fieldWithPath("maxBuyQuantity").type(NUMBER)
                                .description("상품 최대 주문수량"),
                            fieldWithPath("description").type(STRING)
                                .description("상품 설명"),
                            fieldWithPath("mainCategoryId").type(NUMBER)
                                .description("상품 대카테고리 ID"),
                            fieldWithPath("subCategoryId").type(NUMBER)
                                .description("상품 소카테고리 ID")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("상품 삭제 api 호출 시")
    class DeleteItem {

        private static final Long ITEM_ID = 1L;

        @Test
        @DisplayName("성공")
        public void deleteItem() throws Exception {

            // When
            ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/items/{itemId}", ITEM_ID)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isNoContent())
                .andDo(document(
                    "Delete Item",
                    pathParameters(
                        parameterWithName("itemId").description("상품 ID")
                    ))
                );
        }
    }

    @Nested
    @DisplayName("신상품 조회 Api with redis")
    class FindNewItemsWithRedis {

        FindNewItemsResponse findNewItemsResponse = ItemFixture.findNewItemsResponse();

        @Test
        @DisplayName("성공")
        public void findNewItemsWithRedis() throws Exception {
            // Given
            given(itemService.findNewItemsWithRedis(any())).willReturn(findNewItemsResponse);


            // When
            ResultActions resultActions = mockMvc.perform(
                get("/api/v1/items/new-items")
                    .queryParam("lastIdx", "-1")
                    .queryParam("lastItemId", "-1")
                    .queryParam("size", "3")
                    .queryParam("sort", "NEW")
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            resultActions.andExpect(status().isOk())
                .andDo(document("Find New Items with Redis",
                    queryParameters(
                        parameterWithName("lastIdx").description("마지막에 조회한 아이템의 특성값"),
                        parameterWithName("lastItemId").description("마지막에 조회한 아이템 ID"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("sort").description("정렬 기준명")
                    ),
                    responseFields(
                        fieldWithPath("items").type(ARRAY)
                            .description("List of items"),
                        fieldWithPath("items[].itemId").type(NUMBER)
                            .description("상품 ID"),
                        fieldWithPath("items[].name").type(STRING)
                            .description("상품 이름"),
                        fieldWithPath("items[].price").type(NUMBER)
                            .description("상품 가격"),
                        fieldWithPath("items[].discount").type(NUMBER)
                            .description("상품 할인"),
                        fieldWithPath("items[].reviewCount").type(NUMBER)
                            .description("리뷰 수"),
                        fieldWithPath("items[].rate").type(NUMBER)
                            .description("평점")
                    )
                ));
        }
    }
}
