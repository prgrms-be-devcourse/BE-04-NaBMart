package com.prgrms.nabmart.domain.event.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class EventServiceTest {

    private EventService eventService;
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        eventService = new EventService(eventRepository);
    }

    @Nested
    @DisplayName("registerEvent 메서드 실행 시")
    class RegisterEventTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            RegisterEventCommand command = new RegisterEventCommand("TestTitle", "TestDescription");
            Event savedEvent = new Event("TestTitle", "TestDescription");
            when(eventRepository.save(any(Event.class))).thenReturn(
                savedEvent);

            // When
            eventService.registerEvent(command);

            // Then
            verify(eventRepository, times(1)).save(any(Event.class));
        }
    }
}
