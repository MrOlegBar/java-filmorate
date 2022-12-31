package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

@Repository
@Slf4j
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = new FilmDbStorage(jdbcTemplate);
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    public Collection<Film> getPopularFilms(Integer count) throws FilmNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_RATINGS_MPA_VIEW ORDER BY RATE DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, filmDbStorage::mapRowToFilm, count);
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        userDbStorage.getUserById(userId);
        Film film = filmDbStorage.getFilmById(filmId);

        String sqlQuery = "INSERT INTO FILMS_LIKES VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);

        String sqlQueryForAddLike = "UPDATE FILMS SET RATE = RATE + (SELECT COUNT(USER_ID) FROM FILMS_LIKES " +
                "WHERE FILM_ID = ?) WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryForAddLike, filmId, filmId);
        log.info("Добавлен лайк от пользователя с id = {} фильму с id = {}", userId, filmId);
        return film;
    }

    public Film deleteLike(int filmId, int userId) throws UserNotFoundException, FilmNotFoundException
            , IncorrectParameterException {
        userDbStorage.getUserById(userId);
        Film film = filmDbStorage.getFilmById(filmId);

        String sqlQuery = "UPDATE FILMS SET RATE = RATE - (SELECT COUNT(USER_ID) FROM FILMS_LIKES " +
                "WHERE FILM_ID = ?) WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, filmId);

        String sqlQueryForDeleteLike = "DELETE FROM FILMS_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.update(sqlQueryForDeleteLike, filmId, userId) < 1) {
            IncorrectParameterException e = new IncorrectParameterException(
                    String.format("Параметры filmId=%s, userId=%s некорректны.", filmId, userId));
            log.debug("Параметры filmId={}, userId={} некорректны.", filmId, userId);
            throw e;
        } else {
            log.info("Удален лайк от пользователя с id = {} фильму с id = {}", userId, filmId);
        }
        return film;
    }
}