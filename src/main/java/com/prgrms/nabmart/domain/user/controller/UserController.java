package com.prgrms.nabmart.domain.user.controller;

import com.prgrms.nabmart.domain.user.service.UserService;
import com.prgrms.nabmart.domain.user.service.request.FindUserCommand;
import com.prgrms.nabmart.domain.user.service.response.FindUserDetailResponse;
import com.prgrms.nabmart.global.auth.LoginUser;
import com.prgrms.nabmart.global.auth.oauth.client.OAuthRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final OAuthRestClient restClient;

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@LoginUser Long userId) {
        FindUserCommand findUserDetailCommand = FindUserCommand.from(userId);
        FindUserDetailResponse findUserDetailResponse = userService.findUser(findUserDetailCommand);
        restClient.callUnlinkOAuthUser(findUserDetailResponse);
        return ResponseEntity.noContent().build();
    }
}
