package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("filmDbStorage")
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = new GenreDbStorage(jdbcTemplate);
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    @Override
    public Film create(Film film) throws FilmNotFoundException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap(film)).intValue();

        if (film.getGenres() != null) {
            String sqlQuery = "INSERT INTO FILMS_GENRES(FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            }
        }

        if (film.getLikes() != null) {
            String sqlQuery = "INSERT INTO FILMS_LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
            for (Integer userId : film.getLikes()) {
                jdbcTemplate.update(sqlQuery, filmId, userId);
            }
        }

        log.info("Создан фильм: {}", getFilmById(filmId));

        return getFilmById(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() throws FilmNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_RATINGS_MPA_VIEW";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(int filmId) throws FilmNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_RATINGS_MPA_VIEW WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
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

       genreDbStorage.deleteGenresByFilmId(film.getId());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryForUpdateGenres = "MERGE INTO FILMS_GENRES KEY (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQueryForUpdateGenres, film.getId(), genre.getId());
            }
        }

        deleteLikesByFilmId(film.getId());

        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                String sqlQueryForUpdateLikes = "MERGE INTO FILMS_LIKES KEY (FILM_ID, USER_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQueryForUpdateLikes, film.getId(),  userId);
            }
        }

        log.info("Фильм с id = {} обновлен.", film.getId());
        return getFilmById(film.getId());
    }

    public Film getMapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException, FilmNotFoundException {
        return mapRowToFilm(resultSet, rowNum);
    }
    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException, FilmNotFoundException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseDate(Objects.requireNonNull(resultSet.getDate("release_date")).toLocalDate())
                .duration(resultSet.getShort("duration"))
                .mpa(RatingMpa.builder()
                        .id(resultSet.getInt("rating_MPA_id"))
                        .name(resultSet.getString("rating_MPA"))
                        .build())
                .rate(resultSet.getInt("rate"))
                .build();

        if (film.getId() > 0) {
            String sqlQueryForGenres = "SELECT * FROM FILMS_GENRES_VIEW WHERE FILM_ID = ?";

            List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQueryForGenres, genreDbStorage::mapRowToGenre
                    , film.getId()));
            if (genres.contains(Genre.builder().id(0).name(null).build())) {
                genres.clear();
            }
            film.setGenres(genres);

            String sqlQueryForLikes = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ?";

            Set<Integer> likes = new TreeSet<>(jdbcTemplate.query(sqlQueryForLikes, (resultSetLikes, rowNumLikes)
                    -> userDbStorage.mapRowToUserId(resultSetLikes), film.getId()));
            film.setLikes(likes);
        } else {
            log.error("Фильм с id = {} не найден.", film.getId());
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", film.getId()));
        }
        return film;
    }

    private void deleteLikesByFilmId(int filmId) throws UserNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ?";
        Set<Integer> likes = new TreeSet<>(jdbcTemplate.query(sqlQuery, (resultSet, rowNum)
                -> userDbStorage.mapRowToUserId(resultSet), filmId));

        if (!likes.isEmpty()){
            String sqlQueryForDeleteLikes = "DELETE FROM FILMS_LIKES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryForDeleteLikes, filmId);
        }
        log.info("Удалены лайки у фильма с id = {}", filmId);
    }
}