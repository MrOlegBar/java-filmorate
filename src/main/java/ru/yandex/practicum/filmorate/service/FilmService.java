package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws FilmNotFoundException {
        return filmStorage.update(film);
    }

    public Film getFilmById(int filmId) throws FilmNotFoundException {
        return filmStorage.getFilmById(filmId);
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            User user = userStorage.getUserById(userId);
            if (user != null) {
                Set<Long> likesFilm = film.getLikes();
                likesFilm.add((long) userId);
                return film;
            } else {
                throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId));
            }
        } else {
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден", filmId));
        }
    }

    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        if (film != null) {
            User user = userStorage.getUserById(userId);
            if (user != null) {
                Set<Long> likesFilm = film.getLikes();
                likesFilm.remove((long) userId);
                return film;
            } else {
                throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId));
            }
        } else {
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден", filmId));
        }
    }

    public List<Film> getPopularFilms(Integer countFilms) {
        Collection<Film> films = filmStorage.findAll();
        return films.stream()
                .sorted(this::compare)
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return -1 * Integer.compare(f0.getLikes().size(), f1.getLikes().size());
    }
}