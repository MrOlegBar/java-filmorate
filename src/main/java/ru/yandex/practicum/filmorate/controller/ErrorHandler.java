package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({javax.validation.ValidationException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentValidation(MethodArgumentNotValidException e) {
        return new ErrorResponse(String.valueOf(Objects.requireNonNull(e.getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler({UserNotFoundException.class, FilmNotFoundException.class, GenreNotFoundException.class,
            RatingMpaNotFoundException.class, IncorrectParameterException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({DuplicateKeyException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDuplicateKey() {
        return new ErrorResponse("Нарушение уникального индекса или первичного ключа.");
    }
}