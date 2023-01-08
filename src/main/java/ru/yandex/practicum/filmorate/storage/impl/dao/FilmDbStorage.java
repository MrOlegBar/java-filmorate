package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.PreparedStatement;
import java.util.*;

@Component("filmDbStorage")
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MapRowToObject mapRowToObject;


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapRowToObject = new MapRowToObject(jdbcTemplate);
    }

    @Override
    public Film create(Film film) throws FilmNotFoundException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap(film)).intValue();

        if (film.getGenres() != null) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO FILMS_GENRES(FILM_ID, GENRE_ID) VALUES (?, ?)",
                    film.getGenres(),
                    film.getGenres().size(),
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genre.getId());
                    });
        }

        if (film.getLikes() != null) {
            jdbcTemplate.batchUpdate(
                    "INSERT INTO FILMS_LIKES(FILM_ID, USER_ID) VALUES (?, ?)",
                    film.getLikes(),
                    film.getLikes().size(),
                    (PreparedStatement ps, Integer likeId) -> {
                        ps.setInt(1, filmId);
                        ps.setInt(2, likeId);
                    });
        }

        log.info("Создан фильм: {}", getFilmById(filmId));

        return getFilmById(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() throws FilmNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_RATINGS_MPA_VIEW";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> mapRowToObject.mapRowToFilm(resultSet));
    }

    @Override
    public Film getFilmById(int filmId) throws FilmNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_RATINGS_MPA_VIEW WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum) -> mapRowToObject.mapRowToFilm(resultSet), filmId);
    }

    @Override
    public Film update(Film film) throws FilmNotFoundException, UserNotFoundException {
        if (film.getId() < 1) {
            log.error("Фильм с id = {}} не найден.", film.getId());
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", film.getId()));
        }

        String sqlQuery = "UPDATE FILMS SET TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_MPA_ID = ?, RATE = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration()
                , film.getMpa().getId(), film.getRate(), film.getId());

       deleteGenresByFilmId(film.getId());

        if (film.getGenres() != null) {
            jdbcTemplate.batchUpdate(
                    "MERGE INTO FILMS_GENRES KEY (FILM_ID, GENRE_ID) VALUES (?, ?)",
                    film.getGenres(),
                    film.getGenres().size(),
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genre.getId());
                    });
        }

        deleteLikesByFilmId(film.getId());

        if (film.getLikes() != null) {
            jdbcTemplate.batchUpdate(
                    "MERGE INTO FILMS_LIKES KEY (FILM_ID, USER_ID) VALUES (?, ?)",
                    film.getLikes(),
                    film.getLikes().size(),
                    (PreparedStatement ps, Integer likeId) -> {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, likeId);
                    });
        }

        log.info("Фильм с id = {} обновлен.", film.getId());
        return getFilmById(film.getId());
    }
    private void deleteLikesByFilmId(int filmId) throws UserNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ?";
        Set<Integer> likes = new TreeSet<>(jdbcTemplate.query(sqlQuery, (resultSet, rowNum)
                -> mapRowToObject.mapRowToUserId(resultSet), filmId));

        if (!likes.isEmpty()){
            String sqlQueryForDeleteLikes = "DELETE FROM FILMS_LIKES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryForDeleteLikes, filmId);
        }
        log.info("Удалены лайки у фильма с id = {}", filmId);
    }
    private void deleteGenresByFilmId(int filmId) throws GenreNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_GENRES_VIEW WHERE FILM_ID = ?";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQuery, (resultSet, rowNum)
                -> mapRowToObject.mapRowToGenre(resultSet), filmId));

        if (!genres.isEmpty()){
            String sqlQueryForDeleteGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryForDeleteGenres, filmId);
        }
        log.info("Удалены жанры у фильма с id = {}", filmId);
    }
}