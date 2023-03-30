package org.example.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.core.exception.CalendarException;
import org.example.core.exception.ErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CalendarException.class)
    public ResponseEntity<ErrorResponse> handle(CalendarException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(new ErrorResponse(errorCode, e.getMessage()),
                ErrorHttpStatusMapper.mapToStatus(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        final ErrorCode errorCode = ErrorCode.VALIDATION_FAIL;
        log.info(String.valueOf(e.getBindingResult().getFieldError()));
        return new ResponseEntity<>(new ErrorResponse(errorCode,
                Optional.ofNullable(e.getBindingResult().getFieldError())
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .orElse(errorCode.getMessage())
        ), ErrorHttpStatusMapper.mapToStatus(errorCode));
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final ErrorCode errorCode;
        private final String errorMessage;
    }
}