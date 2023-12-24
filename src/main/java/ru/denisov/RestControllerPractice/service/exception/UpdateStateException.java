package ru.denisov.RestControllerPractice.service.exception;

public class UpdateStateException extends RuntimeException{

    public UpdateStateException(String message) {
        super(message);
    }
}
