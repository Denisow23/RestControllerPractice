package ru.denisov.RestControllerPractice.repository.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String format) {
        super(format);
    }
}
