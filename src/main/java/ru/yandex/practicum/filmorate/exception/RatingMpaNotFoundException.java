package ru.yandex.practicum.filmorate.exception;

public class RatingMpaNotFoundException extends RuntimeException {
    public RatingMpaNotFoundException(final String message) {
        super(message);
    }
}