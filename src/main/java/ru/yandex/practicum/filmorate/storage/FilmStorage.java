package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Collection<Film> getAllFilms();

    Film getFilmById(int filmId);

    Film updateFilm(Film film);
}