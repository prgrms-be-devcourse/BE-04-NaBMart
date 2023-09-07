package com.prgrms.nabmart.global.advice;


import com.prgrms.nabmart.global.util.ErrorTemplate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorTemplate> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorTemplate> handleValidationException(
        MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getDefaultMessage()).append("\n");
        }
        log.error(errorMessage.toString(), exception);
        return createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ErrorTemplate> handleRequestParameterException(
        MissingServletRequestParameterException exception) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "요청 파라미터가 누락되었습니다.");
    }

    private ResponseEntity<ErrorTemplate> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ErrorTemplate.of(message));
    }
}
