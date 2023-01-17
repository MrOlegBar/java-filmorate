package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FilmService {
    private final FilmDao filmDbStorage;

    public Film create(Film film) throws FilmNotFoundException {
        return filmDbStorage.create(film);
    }
    public Collection<Film> getAllFilms() throws FilmNotFoundException {
        return filmDbStorage.getAllFilms();
    }

    public Film getFilmById(int filmId) throws FilmNotFoundException {
        return filmDbStorage.getFilmById(filmId);
    }

    public Film update(Film film) throws FilmNotFoundException {
        return filmDbStorage.update(film);
    }
}