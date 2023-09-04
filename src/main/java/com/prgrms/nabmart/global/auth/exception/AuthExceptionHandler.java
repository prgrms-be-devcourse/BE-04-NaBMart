package com.prgrms.nabmart.global.auth.exception;

import com.prgrms.nabmart.global.util.ErrorTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorTemplate> authExHandle(AuthException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(ex.getMessage()));
    }

    @ExceptionHandler(OAuthUnlinkFailureException.class)
    public ResponseEntity<ErrorTemplate> oAuthUnlinkFailureExHandle(OAuthUnlinkFailureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorTemplate.of(ex.getMessage()));
    }
}
