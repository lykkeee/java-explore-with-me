package ru.practicum.exception;

public class ValidateException extends RuntimeException {
    public ValidateException() {
        super();
    }

    public ValidateException(String message) {
        super(message);
    }
}
