package ru.denisov.RestControllerPractice.repository.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String format) {
        super(format);
    }
}
