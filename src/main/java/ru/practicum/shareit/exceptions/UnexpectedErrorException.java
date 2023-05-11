package ru.practicum.shareit.exceptions;

public class UnexpectedErrorException extends RuntimeException {

    public UnexpectedErrorException(String s) {
        super(s);
    }
}
