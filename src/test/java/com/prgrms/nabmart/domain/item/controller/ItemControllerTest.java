package com.prgrms.nabmart.domain.item.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.base.BaseControllerTest;
import com.prgrms.nabmart.domain.item.service.request.FindItemsByMainCategoryCommand;
import com.prgrms.nabmart.domain.item.service.response.FindItemsResponse;
import com.prgrms.nabmart.domain.item.support.ItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ItemControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("findItemsByMainCategory 메서드 호출 시")
    class FindItemsByMainCategory {

        FindItemsResponse findItemsResponse = ItemFixture.findItemsResponse();
        String mainCategoryName = "main category";
        FindItemsByMainCategoryCommand findItemsByMainCategoryCommand = ItemFixture.findItemsByMainCategoryCommand(
            mainCategoryName);

        @Test
        @DisplayName("성공")
        void findItemsByMainCategory() throws Exception {
            // Given
            when(itemService.findItemsByMainCategory(findItemsByMainCategoryCommand)).thenReturn(
                findItemsResponse);

            // Then&When
            mockMvc.perform(get("/api/v1/items")
                    .queryParam("previousItemId", "5")
                    .queryParam("size", "3")
                    .queryParam("main", mainCategoryName)
                    .queryParam("sort", "NEW"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                    queryParameters(
                        parameterWithName("previousItemId").description("마지막에 조회한 아이템 Id"),
                        parameterWithName("size").description("조회할 아이템 수"),
                        parameterWithName("main").description("대카테고리명"),
                        parameterWithName("sort").description("정렬 기준명")
                    )
                ));

        }

    }
}
