package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmDbStorage;

    public Film createFilm(Film film) {
        return filmDbStorage.createFilm(film);
    }
    public Film getFilmById(int filmId) {
        return filmDbStorage.getFilmById(filmId);
    }

    public Collection<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public Collection<Film> deleteFilmById(int filmId) {
        return filmDbStorage.deleteFilmById(filmId);
    }
    public Collection<Film> deleteAllFilms() {
        return filmDbStorage.deleteAllFilms();
    }
    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        return filmDbStorage.addLike(filmId, userId);
    }
    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        return filmDbStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmDbStorage.getPopularFilms(count);
    }
}