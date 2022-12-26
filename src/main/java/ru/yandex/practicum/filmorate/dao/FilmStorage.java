package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Film getFilmById(int filmId);

    Collection<Film> getAllFilms();

    Film updateFilm(Film film);

    Collection<Film> deleteFilmById(int id);

    Collection<Film> deleteAllFilms();

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    Collection<Film> getPopularFilms(Integer countFilms);
}