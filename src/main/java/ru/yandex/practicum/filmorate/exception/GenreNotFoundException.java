package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(final String message) {
        super(message);
    }
}