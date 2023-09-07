package com.prgrms.nabmart.domain.category.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.nabmart.domain.category.fixture.CategoryFixture;
import com.prgrms.nabmart.domain.category.service.CategoryService;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import com.prgrms.nabmart.domain.category.service.request.RegisterSubCategoryCommand;
import com.prgrms.nabmart.domain.category.service.response.FindMainCategoriesResponse;
import com.prgrms.nabmart.domain.category.service.response.FindSubCategoriesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Nested
    @DisplayName("대카테고리 저장하는 api 호출 시")
    class SaveMainCategoryApi {

        @Test
        @DisplayName("성공")
        public void saveMainCategory() throws Exception {
            // Given
            RegisterMainCategoryCommand command = new RegisterMainCategoryCommand("TestCategory");
            when(categoryService.saveMainCategory(command)).thenReturn(1L);

            // When & Then
            mockMvc.perform(post("/api/v1/main-categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/main-categories/1"))
                .andDo(print())
                .andDo(document(
                    "Save MainCategory",
                    preprocessRequest(
                        prettyPrint()
                    ),
                    requestFields(
                        fieldWithPath("name").type(STRING)
                            .description("대카테고리명")
                    )
                ));

            verify(categoryService, times(1)).saveMainCategory(command);
        }
    }

    @Nested
    @DisplayName("소카테고리 저장하는 api 호출 시")
    class SaveSubCategoryApi {

        @Test
        @DisplayName("성공")
        public void saveSubCategory() throws Exception {
            // Given
            RegisterSubCategoryCommand command = new RegisterSubCategoryCommand(1L, "sub-category");
            when(categoryService.saveSubCategory(command)).thenReturn(1L);

            // When & Then

            mockMvc.perform(post("/api/v1/sub-categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/sub-categories/1"))
                .andDo(print())
                .andDo(document(
                    "Save SubCategory",
                    preprocessRequest(
                        prettyPrint()
                    ),
                    requestFields(
                        fieldWithPath("mainCategoryId").type(NUMBER)
                            .description("대카테고리 Id"),
                        fieldWithPath("name").type(STRING)
                            .description("소카테고리명")
                    )
                ));

            verify(categoryService, times(1)).saveSubCategory(command);
        }
    }

    @Nested
    @DisplayName("모든 대카테고리 조회 api 호출 시")
    class findAllMainCategoriesApi {

        @Test
        @DisplayName("성공")
        public void findAllMainCategories() throws Exception {
            // Given
            FindMainCategoriesResponse mainCategoriesResponse = CategoryFixture.findMainCategoriesResponse();
            when(categoryService.findAllMainCategories()).thenReturn(mainCategoriesResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/categories")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document(
                    "Find All MainCategories",
                    preprocessRequest(
                        prettyPrint()
                    ),
                    responseFields(
                        fieldWithPath("mainCategoryNames").type(ARRAY)
                            .description("대카테고리 리스트")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("소카테고리 조회 api 호출 시")
    class findSubCategoriesApi {

        @Test
        @DisplayName("성공")
        public void findSubCategories() throws Exception {
            // Given
            Long mainCategoryId = 1L;
            FindSubCategoriesResponse subCategoriesResponse = CategoryFixture.findSubCategoriesResponse();
            when(categoryService.findSubCategoriesByMainCategory(mainCategoryId)).thenReturn(
                subCategoriesResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/categories/{mainCategoryId}", mainCategoryId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document(
                    "Find SubCategories By MainCategory",
                    preprocessRequest(
                        prettyPrint()
                    ),
                    pathParameters(
                        parameterWithName("mainCategoryId").description("대카테고리 Id")
                    ),
                    responseFields(
                        fieldWithPath("subCategoryNames").type(ARRAY)
                            .description("소카테고리 리스트")
                    )
                ));
        }
    }
}
