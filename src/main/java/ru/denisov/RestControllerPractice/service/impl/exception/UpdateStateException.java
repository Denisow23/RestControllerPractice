package ru.denisov.RestControllerPractice.service.impl.exception;

public class UpdateStateException extends RuntimeException{

    public UpdateStateException(String message) {
        super(message);
    }
}
