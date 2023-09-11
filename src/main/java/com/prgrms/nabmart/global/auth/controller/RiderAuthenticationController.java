package com.prgrms.nabmart.global.auth.controller;

import com.prgrms.nabmart.global.auth.controller.request.RiderLoginRequest;
import com.prgrms.nabmart.global.auth.service.RiderAuthenticationService;
import com.prgrms.nabmart.global.auth.service.request.RiderLoginCommand;
import com.prgrms.nabmart.global.auth.service.response.RiderLoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RiderAuthenticationController {

    private final RiderAuthenticationService riderAuthenticationService;

    @PostMapping("/riders/login")
    public ResponseEntity<RiderLoginResponse> riderLogin(
        @RequestBody @Valid RiderLoginRequest riderLoginRequest) {
        RiderLoginCommand riderLoginCommand
            = RiderLoginCommand.of(riderLoginRequest.username(), riderLoginRequest.password());
        RiderLoginResponse riderLoginResponse
            = riderAuthenticationService.riderLogin(riderLoginCommand);
        return ResponseEntity.ok(riderLoginResponse);
    }
}
