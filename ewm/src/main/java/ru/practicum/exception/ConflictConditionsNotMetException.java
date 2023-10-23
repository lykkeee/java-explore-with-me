package ru.practicum.exception;

public class ConflictConditionsNotMetException extends RuntimeException {
    public ConflictConditionsNotMetException() {
        super();
    }

    public ConflictConditionsNotMetException(String message) {
        super(message);
    }
}
