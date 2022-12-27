package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre getGenreById(int filmId);

    Collection<Genre> getAllGenres();
}
