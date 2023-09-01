package com.prgrms.nabmart.domain.event.controller;

import com.prgrms.nabmart.domain.event.domain.RegisterEventRequest;
import com.prgrms.nabmart.domain.event.service.EventService;
import com.prgrms.nabmart.global.auth.LoginUser;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerEvent(
        @LoginUser Long userId,
        @RequestBody @Valid final RegisterEventRequest registerEventRequest,
        HttpServletResponse response
    ) {
        Long eventId = eventService.registerEvent(userId, registerEventRequest);
        response.setHeader("Location", "/v1/events/" + eventId);
    }

}
