package org.example.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CalendarException extends RuntimeException {
    private final ErrorCode errorCode;

}
