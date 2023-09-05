package com.prgrms.nabmart.domain.event.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.prgrms.nabmart.domain.category.MainCategory;
import com.prgrms.nabmart.domain.category.SubCategory;
import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.domain.EventItem;
import com.prgrms.nabmart.domain.event.exception.DuplicateEventItemException;
import com.prgrms.nabmart.domain.event.repository.EventItemRepository;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventItemsCommand;
import com.prgrms.nabmart.domain.item.domain.Item;
import com.prgrms.nabmart.domain.item.exception.NotFoundItemException;
import com.prgrms.nabmart.domain.item.repository.ItemRepository;
import com.prgrms.nabmart.global.fixture.CategoryFixture;
import com.prgrms.nabmart.global.fixture.EventFixture;
import com.prgrms.nabmart.global.fixture.EventItemFixture;
import com.prgrms.nabmart.global.fixture.ItemFixture;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventItemServiceTest {

    @InjectMocks
    private EventItemService eventItemService;

    @Mock
    private EventItemRepository eventItemRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ItemRepository itemRepository;

    Event givenEvent;
    EventItem givenEventItem;
    Item givenItem;
    MainCategory givenMainCategory;
    SubCategory givenSubCategory;

    @BeforeEach
    void setUp() {
        givenEvent = EventFixture.event();
        givenMainCategory = CategoryFixture.mainCategory();
        givenSubCategory = CategoryFixture.subCategory(givenMainCategory);
        givenItem = ItemFixture.item(givenMainCategory, givenSubCategory);
        givenEventItem = EventItemFixture.eventItem(givenEvent, givenItem);
    }

    @Nested
    @DisplayName("registerEventItems 메서드 실행 시")
    class RegisterEventItemsTest {

        RegisterEventItemsCommand command = RegisterEventItemsCommand.from(1L,
            Arrays.asList(1L));

        @Test
        @DisplayName("성공")
        void success() {
            // Given
            given(eventRepository.findById(any())).willReturn(Optional.ofNullable(givenEvent));
            given(itemRepository.findById(any())).willReturn(Optional.ofNullable(givenItem));
            given(eventItemRepository.existsByEventAndItem(any(), any())).willReturn(false);
            given(eventItemRepository.saveAll(any())).willReturn(Arrays.asList(givenEventItem));

            // When
            eventItemService.registerEventItems(command);

            // Then
            then(eventItemRepository).should().saveAll(any());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 아이템")
        void throwExceptionWhenItemNotFound() {
            // Given
            given(eventRepository.findById(any())).willReturn(Optional.ofNullable(givenEvent));
            given(itemRepository.findById(any())).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> eventItemService.registerEventItems(command))
                .isInstanceOf(NotFoundItemException.class);
        }

        @Test
        @DisplayName("예외: 이벤트 아이템 중복")
        void throwExceptionWhenDuplicateEventItem() {
            // Given
            given(eventRepository.findById(any())).willReturn(Optional.ofNullable(givenEvent));
            given(itemRepository.findById(any())).willReturn(Optional.ofNullable(givenItem));
            given(eventItemRepository.existsByEventAndItem(any(), any())).willReturn(true);

            // When & Then
            assertThatThrownBy(() -> eventItemService.registerEventItems(command))
                .isInstanceOf(DuplicateEventItemException.class);
        }
    }
}
