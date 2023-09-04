package com.prgrms.nabmart.domain.event.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.nabmart.domain.event.service.EventService;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventCommand;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse.FindEventResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(EventControllerTest.class)
public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Nested
    @DisplayName("이벤트 전체 조회하는 api 호출 시")
    class FindEventsApi {

        @Test
        @DisplayName("성공")
        public void success() throws Exception {
            // Given
            FindEventsResponse eventResponses = FindEventsResponse.of(List.of(
                new FindEventResponse(1L, "Event 1", "Description 1"),
                new FindEventResponse(2L, "Event 2", "Description 2")
            ));
            when(eventService.findEvents()).thenReturn(eventResponses);

            // When & Then
            mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(eventResponses)));

            verify(eventService, times(1)).findEvents();
        }
    }
}
