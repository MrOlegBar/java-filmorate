package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Collection<Film> getAllFilms();

    Film getFilmById(int filmId);

    Film update(Film film);
}