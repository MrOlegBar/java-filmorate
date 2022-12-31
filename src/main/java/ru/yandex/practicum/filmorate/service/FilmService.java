package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmDbStorage;

    public Film createFilm(Film film) throws FilmNotFoundException {
        return filmDbStorage.createFilm(film);
    }
    public Collection<Film> getAllFilms() throws FilmNotFoundException {
        return filmDbStorage.getAllFilms();
    }

    public Film getFilmById(int filmId) throws FilmNotFoundException {
        return filmDbStorage.getFilmById(filmId);
    }

    public Film updateFilm(Film film) throws FilmNotFoundException, UserNotFoundException {
        return filmDbStorage.updateFilm(film);
    }
}