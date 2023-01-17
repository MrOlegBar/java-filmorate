package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface FilmDao {
    Film create(Film film);

    Collection<Film> getAllFilms();

    Film getFilmById(int filmId);

    Film update(Film film);
}