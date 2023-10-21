package ru.practicum.exception;

public class ValidateTimeException extends RuntimeException {
    public ValidateTimeException() {
        super();
    }

    public ValidateTimeException(String message) {
        super(message);
    }
}
