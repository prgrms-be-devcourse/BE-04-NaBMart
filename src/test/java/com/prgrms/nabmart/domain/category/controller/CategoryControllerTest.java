package com.prgrms.nabmart.domain.category.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.domain.category.service.CategoryService;
import com.prgrms.nabmart.domain.category.service.request.RegisterMainCategoryCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryController(categoryService)).build();
    }

    @Nested
    @DisplayName("카테고리 저장하는 api 호출 시")
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
                    .content("{\"name\":\"TestCategory\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/main-categories/1"));

            verify(categoryService, times(1)).saveMainCategory(command);
        }
    }
}
