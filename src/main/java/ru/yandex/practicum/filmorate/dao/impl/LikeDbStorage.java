package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.impl.film.FilmDbStorage;
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

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = new FilmDbStorage(jdbcTemplate);
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        String sqlQueryForFilmLikes = "INSERT INTO FILMS_LIKES VALUES (?, ?)";
        jdbcTemplate.update(sqlQueryForFilmLikes, filmId, userId);

        String sqlQueryForFilms = "UPDATE FILMS SET RATE = RATE + (SELECT COUNT(USER_ID) FROM FILMS_LIKES WHERE FILM_ID = ?) " +
                "WHERE FILM_ID = ?;";
        jdbcTemplate.update(sqlQueryForFilms, filmId, filmId);
        return filmDbStorage.getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) throws IncorrectParameterException {
        String sqlQueryForFilms = "UPDATE FILMS SET RATE = RATE - (SELECT COUNT(USER_ID) FROM FILMS_LIKES WHERE FILM_ID = ?) " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryForFilms, filmId, filmId);

        String sqlQueryForFilmLikes = "DELETE FROM FILMS_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.update(sqlQueryForFilmLikes, filmId, userId) < 1) {
            IncorrectParameterException e = new IncorrectParameterException(
                    String.format("Параметры filmId=%s, userId=%s некорректны.", filmId, userId));
            log.debug("Параметры filmId={}, userId={} некорректны.", filmId, userId);
            throw e;
        }

        return filmDbStorage.getFilmById(filmId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT * FROM FILMS_RATINGS_MPA_VIEW ORDER BY RATE DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, filmDbStorage::mapRowToFilm, count);
    }
}