package com.prgrms.nabmart.domain.category.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.nabmart.domain.category.service.CategoryService;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import com.prgrms.nabmart.domain.category.service.request.RegisterSubCategoryCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
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
        public void success() throws Exception {
            // Given
            RegisterMainCategoryCommand command = new RegisterMainCategoryCommand("TestCategory");
            when(categoryService.saveMainCategory(command)).thenReturn(1L);

            // When & Then
            mockMvc.perform(post("/api/v1/main-categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/main-categories/1"));

            verify(categoryService, times(1)).saveMainCategory(command);
        }
    }

    @Nested
    @DisplayName("소카테고리 저장하는 api 호출 시")
    class SaveSubCategoryApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            RegisterSubCategoryCommand command = new RegisterSubCategoryCommand(1L, "sub-category");
            when(categoryService.saveSubCategory(command)).thenReturn(1L);

            // When & Then
            mockMvc.perform(post("/api/v1/sub-categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/sub-categories/1"));

            verify(categoryService, times(1)).saveSubCategory(command);
        }
    }
}
