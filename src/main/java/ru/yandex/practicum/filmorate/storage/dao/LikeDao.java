package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.Collection;

@Repository
@Getter
@Slf4j
public class LikeDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    public LikeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
        this.filmDbStorage = new FilmDbStorage(jdbcTemplate);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT * FROM films_ratings_mpa_view ORDER BY rate DESC LIMIT ?";
        Collection<Film> films = jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> FilmMapper.mapRowToFilm(resultSet),
                count);

        films.forEach(film -> {
            film.setLikes(filmDbStorage.getLikes(film.getId()));
            film.setGenres(filmDbStorage.getGenres(film.getId()));
        });
        return films;
    }

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        userDbStorage.getUserById(userId);
        filmDbStorage.getFilmById(filmId);

        String sqlQuery = "INSERT INTO films_likes VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);

        String sqlQueryForAddLike = "UPDATE FILMS SET RATE = RATE + (SELECT COUNT(USER_ID) FROM FILMS_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?) WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryForAddLike, filmId, userId, filmId);

        Film filmWithLike = filmDbStorage.getFilmById(filmId);
        log.info("Добавлен лайк от пользователя с userId = {} фильму: {}", userId, filmWithLike);
        return filmWithLike;
    }

    public Film deleteLike(int filmId, int userId) throws UserNotFoundException, FilmNotFoundException,
            IncorrectParameterException {
        userDbStorage.getUserById(userId);
        filmDbStorage.getFilmById(filmId);

        String sqlQuery = "UPDATE FILMS SET RATE = RATE - (SELECT COUNT(USER_ID) FROM FILMS_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?) WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId, filmId);

        String sqlQueryForDeleteLike = "DELETE FROM FILMS_LIKES WHERE FILM_ID = ? AND USER_ID = ?";

        try {
            jdbcTemplate.update(sqlQueryForDeleteLike, filmId, userId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Параметры filmId={}, userId={} некорректны.", filmId, userId);
            throw new IncorrectParameterException(String.format("Параметры filmId = %s, userId = %s некорректны."
                    , filmId, userId));
        }

        Film filmWithoutLike = filmDbStorage.getFilmById(filmId);
        log.info("Удален лайк от пользователя с userId = {} фильму: {}", userId, filmWithoutLike);
        return filmWithoutLike;
    }
}