package com.prgrms.nabmart.global.auth.controller;

import com.prgrms.nabmart.global.auth.controller.request.SignupRiderRequest;
import com.prgrms.nabmart.global.auth.service.request.SignupRiderCommand;
import com.prgrms.nabmart.global.auth.controller.request.RiderLoginRequest;
import com.prgrms.nabmart.global.auth.service.RiderAuthenticationService;
import com.prgrms.nabmart.global.auth.service.request.RiderLoginCommand;
import com.prgrms.nabmart.global.auth.service.response.RiderLoginResponse;
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
@RequestMapping("/api/v1")
public class RiderAuthenticationController {

    private static final String BASE_URL = "/api/v1/riders/";

    private final RiderAuthenticationService riderAuthenticationService;

    @PostMapping("/riders/signup")
    public ResponseEntity<Void> signupRider(@RequestBody @Valid SignupRiderRequest signupRiderRequest) {
        SignupRiderCommand signupRiderCommand = SignupRiderCommand.of(
            signupRiderRequest.username(),
            signupRiderRequest.password(),
            signupRiderRequest.address());
        Long riderId = riderAuthenticationService.signupRider(signupRiderCommand);
        URI location = URI.create(BASE_URL + riderId);
        return ResponseEntity.created(location).build();
    }

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
