package com.prgrms.nabmart.global.auth.controller;

import com.prgrms.nabmart.global.auth.exception.AuthException;
import com.prgrms.nabmart.global.auth.exception.OAuthUnlinkFailureException;
import com.prgrms.nabmart.global.auth.exception.UnAuthenticationException;
import com.prgrms.nabmart.global.util.ErrorTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorTemplate> authExHandle(AuthException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(ex.getMessage()));
    }

    @ExceptionHandler({OAuthUnlinkFailureException.class, UnAuthenticationException.class})
    public ResponseEntity<ErrorTemplate> authenticationFailExHand(AuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorTemplate.of(ex.getMessage()));
    }
}
