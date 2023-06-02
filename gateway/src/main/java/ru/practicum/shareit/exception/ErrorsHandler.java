package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorsHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgumentException(final IllegalArgumentException e) {
        return Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
    }
}
