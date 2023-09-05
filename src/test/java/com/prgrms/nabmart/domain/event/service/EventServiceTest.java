package com.prgrms.nabmart.domain.event.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.request.FindEventDetailCommand;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventCommand;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.global.fixture.CategoryFixture;
import com.prgrms.nabmart.global.fixture.EventFixture;
import com.prgrms.nabmart.global.fixture.ItemFixture;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Nested
    @DisplayName("findEvents 메서드 실행 시")
    class FindEventsTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            List<Event> events = Arrays.asList(
                new Event("title 1", "description 1"),
                new Event("title 2", "description 2")
            );
            when(eventRepository.findAllByOrderByCreatedAtDesc()).thenReturn(events);

            // When
            eventService.findEvents();

            // Then
            verify(eventRepository, times(1)).findAllByOrderByCreatedAtDesc();
        }
    }

    @Nested
    @DisplayName("findEventDetail 메서드 실행 시")
    class FindEventDetailTests {

        @Test
        @DisplayName("성공")
        public void success() {
            // Given
            Event event = EventFixture.event();
            Item item = ItemFixture.item(CategoryFixture.mainCategory(),
                CategoryFixture.subCategory(CategoryFixture.mainCategory()));
            EventItem eventItem = new EventItem(event, item);
            FindEventDetailCommand command = FindEventDetailCommand.from(event.getEventId());
            when(eventRepository.findByIdWithEventItems(event.getEventId())).thenReturn(
                Optional.of(event));

            // When
            eventService.findEventDetail(command);

            // Then
            verify(eventRepository, times(1)).findByIdWithEventItems(command.eventId());
        }
    }
}
