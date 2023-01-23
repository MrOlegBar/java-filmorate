package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;

import java.sql.PreparedStatement;
import java.util.*;

@Component("filmDbStorage")
@Repository
@Slf4j
@AllArgsConstructor
public class FilmDbStorage implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) throws FilmNotFoundException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(filmId);

        putGenres(film);

        Film createdFilm = getFilmById(filmId);
        log.info("Создан фильм: {}.", createdFilm);

        return createdFilm;
    }

    @Override
    public Film getFilmById(int filmId) throws FilmNotFoundException {
        String sqlQuery = "SELECT * FROM films_ratings_mpa_view WHERE FILM_ID = ?";
        Film film;

        try {
            film = jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum)
                    -> FilmMapper.mapRowToFilm(resultSet), filmId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Фильм с filmId = {} не найден.", filmId);
            throw new FilmNotFoundException(String.format("Фильм с filmId = %s не найден.", filmId));
        }

        if (film != null) {
            film.setLikes(getLikes(film.getId()));
            film.setGenres(getGenres(film.getId()));
        }

        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films_ratings_mpa_view";
        Collection<Film> films = jdbcTemplate.query(sqlQuery, (resultSet, rowNum)
                -> FilmMapper.mapRowToFilm(resultSet));

        films.forEach(film -> {
            film.setLikes(getLikes(film.getId()));
            film.setGenres(getGenres(film.getId()));
        });
        return films;
    }

    @Override
    public Film update(Film film) throws FilmNotFoundException {
        getFilmById(film.getId());

        String sqlQuery = "UPDATE FILMS SET TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_MPA_ID = ?, RATE = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration()
                , film.getMpa().getId(), film.getRate(), film.getId());

        deleteGenresByFilmId(film.getId());

        putGenres(film);

        Film updatedFilm = getFilmById(film.getId());
        log.info("Обновлен фильм: {}.", updatedFilm);
        return updatedFilm;
    }

    private void putGenres(Film film) {
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

        log.info("Добавлены жанры фильму с filmId = {}", film.getId());
    }

    private void deleteGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT * FROM films_genres_view WHERE FILM_ID = ?";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQuery, (resultSet, rowNum)
                -> GenreMapper.mapRowToGenre(resultSet), filmId));

        if (genres.contains(null)) {
            genres.clear();
        }

        if (!genres.isEmpty()){
            String sqlQueryForDeleteGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryForDeleteGenres, filmId);
        }

        log.info("Удалены жанры фильма с filmId = {}", filmId);
    }

    public Set<Integer> getLikes(int filmId) {
        String sqlQueryForLikes = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ?";

        return new TreeSet<>(jdbcTemplate.query(sqlQueryForLikes, (resultSetLikes, rowNumLikes)
                    -> LikeMapper.mapRowToUserId(resultSetLikes), filmId));
    }

    public List<Genre> getGenres(int filmId) {
        String sqlQueryForGenres = "SELECT * FROM films_genres_view WHERE FILM_ID = ?";

        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQueryForGenres, (resultSet1, rowNum)
                -> GenreMapper.mapRowToGenre(resultSet1), filmId));

        if (genres.contains(null)) {
            genres.clear();
        }

        return genres;
    }
}