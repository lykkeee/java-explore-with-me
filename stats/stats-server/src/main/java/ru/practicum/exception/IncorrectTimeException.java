package ru.practicum.exception;

public class IncorrectTimeException extends RuntimeException {
    public IncorrectTimeException() {
        super();
    }

    public IncorrectTimeException(String message) {
        super(message);
    }
}
