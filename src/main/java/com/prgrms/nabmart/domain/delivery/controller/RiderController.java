package com.prgrms.nabmart.domain.delivery.controller;

import com.prgrms.nabmart.domain.delivery.controller.request.RiderSignupRequest;
import com.prgrms.nabmart.domain.delivery.service.RiderService;
import com.prgrms.nabmart.domain.delivery.service.request.RiderSignupCommand;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders")
public class RiderController {

    private static final String BASE_URL = "/api/v1/riders/";

    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<Void> riderSignup(@RequestBody @Valid RiderSignupRequest riderSignupRequest) {
        RiderSignupCommand riderSignupCommand = RiderSignupCommand.of(
            riderSignupRequest.username(),
            riderSignupRequest.password(),
            riderSignupRequest.address());
        Long riderId = riderService.riderSignup(riderSignupCommand);
        URI location = URI.create(BASE_URL + riderId);
        return ResponseEntity.created(location).build();
    }
}
