package com.prgrms.nabmart.domain.event.service;

import com.prgrms.nabmart.domain.event.domain.Event;
import com.prgrms.nabmart.domain.event.exception.NotFoundEventException;
import com.prgrms.nabmart.domain.event.repository.EventRepository;
import com.prgrms.nabmart.domain.event.service.request.FindEventDetailCommand;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventCommand;
import com.prgrms.nabmart.domain.event.service.response.FindEventDetailResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventDetailResponse.EventDetailResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventDetailResponse.EventItemResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse.FindEventResponse;
import com.prgrms.nabmart.domain.review.Review;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Long registerEvent(RegisterEventCommand registerEventCommand) {
        Event event = new Event(registerEventCommand.title(), registerEventCommand.description());
        Event registered = eventRepository.save(event);
        return registered.getEventId();
    }

    @Transactional(readOnly = true)
    public FindEventsResponse findEvents() {
        List<Event> events = eventRepository.findAllByOrderByCreatedAtDesc();
        return FindEventsResponse.of(events.stream()
            .map(event -> new FindEventResponse(
                event.getEventId(),
                event.getTitle(),
                event.getDescription()
            )).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public FindEventDetailResponse findEventDetail(FindEventDetailCommand findEventDetailCommand) {
        Event event = eventRepository.findByIdWithEventItems(findEventDetailCommand.eventId())
            .orElseThrow(() -> new NotFoundEventException("존재하지 않는 이벤트입니다."));

        EventDetailResponse eventDetailResponse = new EventDetailResponse(event.getEventId(),
            event.getTitle(), event.getDescription());

        List<EventItemResponse> eventItemResponses = event.getEventItemList().stream()
            .map(eventItem -> new EventItemResponse(
                    eventItem.getEventItemId(),
                    eventItem.getItem().getName(),
                    eventItem.getItem().getPrice(),
                    eventItem.getItem().getDiscount(),
                    eventItem.getItem().getReviewList().size(),
                    eventItem.getItem().getLikeItemList().size(),
                    eventItem.getItem().getReviewList().stream().mapToDouble(Review::getRate).average()
                        .orElse(0.0)
                )
            ).collect(Collectors.toList());

        return FindEventDetailResponse.of(eventDetailResponse, eventItemResponses);
    }
}
