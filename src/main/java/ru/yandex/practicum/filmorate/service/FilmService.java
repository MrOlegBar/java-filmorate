package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
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
}