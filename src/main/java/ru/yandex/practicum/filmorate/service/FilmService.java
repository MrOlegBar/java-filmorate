package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    /*PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    Если значение параметра count не задано, верните первые 10.*/
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

    public List<Film> getPopularFilms(int countFilms) {
        List<Film> popularFilms = new ArrayList<>();


        return popularFilms;
    }
}