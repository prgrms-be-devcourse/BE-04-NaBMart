package com.prgrms.nabmart.domain.event.controller;

import com.prgrms.nabmart.domain.event.controller.request.RegisterEventRequest;
import com.prgrms.nabmart.domain.event.service.EventService;
import com.prgrms.nabmart.domain.event.service.request.FindEventDetailCommand;
import com.prgrms.nabmart.domain.event.service.request.RegisterEventCommand;
import com.prgrms.nabmart.domain.event.service.response.FindEventDetailResponse;
import com.prgrms.nabmart.domain.event.service.response.FindEventsResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private static final String BASE_URL = "/api/v1/events/";
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Void> registerEvent(
        @RequestBody @Valid RegisterEventRequest registerEventRequest
    ) {
        RegisterEventCommand registerEventCommand = RegisterEventCommand.from(
            registerEventRequest.title(), registerEventRequest.description());
        Long eventId = eventService.registerEvent(registerEventCommand);
        URI location = URI.create(BASE_URL + eventId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<FindEventsResponse> findEvents() {
        return ResponseEntity.ok(eventService.findEvents());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<FindEventDetailResponse> findEventDetail(
        @PathVariable final Long eventId
    ) {
        FindEventDetailCommand findEventDetailCommand = FindEventDetailCommand.from(eventId);
        return ResponseEntity.ok(eventService.findEventDetail(findEventDetailCommand));
    }
}
