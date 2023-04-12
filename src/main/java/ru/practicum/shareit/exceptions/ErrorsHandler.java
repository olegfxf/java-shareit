package ru.practicum.shareit.exceptions;

import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.messages.HandlerMessages;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidationException(final ValidationException e) {
        log.info(String.valueOf(HandlerMessages.ERROR_400), e.getMessage());
        return Map.of(String.valueOf(HandlerMessages.VALID), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFoundException(final NotFoundException e) {
        log.info(String.valueOf(HandlerMessages.ERROR_404), e.getMessage());
        return Map.of(String.valueOf(HandlerMessages.NOT_FOUND), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handlerConflictException(final ConflictException e) {
        log.info(String.valueOf(HandlerMessages.ERROR_409), e.getMessage());
        return Map.of(String.valueOf(HandlerMessages.CONFLICT), e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerInternalException(final ExecutionControl.InternalException e) {
        log.info(String.valueOf(HandlerMessages.ERROR_500), e.getMessage());
        return Map.of(String.valueOf(HandlerMessages.SERVER_ERROR), e.getMessage());
    }
}
