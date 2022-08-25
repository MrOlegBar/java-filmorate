package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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