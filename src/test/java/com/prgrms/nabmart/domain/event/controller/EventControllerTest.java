package com.prgrms.nabmart.domain.event.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.nabmart.domain.event.service.EventService;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new EventController(eventService)).build();
    }

    @Nested
    @DisplayName("이벤트 등록하는 api 호출 시")
    class RegisterEventApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            RegisterEventCommand command = new RegisterEventCommand("TestTitle", "TestDescription");
            when(eventService.registerEvent(command)).thenReturn(1L);

            // When & Then
            mockMvc.perform(post("/api/v1/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"TestTitle\",\"description\": \"TestDescription\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/events/1"));

            verify(eventService, times(1)).registerEvent(command);
        }
    }
}
