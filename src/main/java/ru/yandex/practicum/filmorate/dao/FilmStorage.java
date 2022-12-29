package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Film getFilmById(int filmId);

    Collection<Film> getAllFilms();

    Film updateFilm(Film film);
}