package ru.yandex.practicum.filmorate.dao.impl.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.impl.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("filmDbStorage")
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    GenreDbStorage genreDbStorage;
    UserDbStorage userDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = new GenreDbStorage(jdbcTemplate);
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(saveFilmAndReturnId(film));

        if (film.getGenres() != null) {
            String sqlQueryForFilmGenre = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQueryForFilmGenre,
                        film.getId(),
                        genre.getId());
            }
        }

        if (film.getLikes() != null) {
            String sqlQueryForFilmLikes = "INSERT INTO FILM_LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
            for (Integer userId : film.getLikes()) {
                jdbcTemplate.update(sqlQueryForFilmLikes,
                        film.getId(),
                        userId);
            }
        }

        return getFilmById(film.getId());
    }
    @Override
    public Film getFilmById(int filmId) {
        String sqlQueryForFilms = "SELECT * FROM FILMS_RATING_MPA_VIEW WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQueryForFilms, this::mapRowToFilm, filmId);
    }
    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM FILMS_RATING_MPA_VIEW";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }
    @Override
    public Film updateFilm(Film film) {
        String sqlQueryForFilms = "UPDATE FILMS SET TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_MPA_ID = ?, RATE = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryForFilms
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getRate()
                , film.getId());

        if (!genreDbStorage.getAllGenres().isEmpty()) {
            deleteGenresByFilmId(film.getId());
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryForGenres = "MERGE INTO FILM_GENRE KEY (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQueryForGenres,
                        film.getId(),
                        genre.getId());
            }
        }

        if (!userDbStorage.getAllLikes().isEmpty()){
            deleteLikesByFilmId(film.getId());
        }
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                String sqlQueryForGenres = "MERGE INTO FILM_LIKES KEY (FILM_ID, USER_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQueryForGenres,
                        film.getId(),
                        userId);
            }
        }

        return getFilmById(film.getId());
    }
    public Collection<Film> deleteFilmById(int filmId) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        deleteGenresByFilmId(filmId);
        deleteLikesByFilmId(filmId);
        return getAllFilms();
    }
    public Collection<Film> deleteAllFilms() {
        String sqlQueryForFilms = "DELETE FROM FILMS";
        jdbcTemplate.update(sqlQueryForFilms);

        String sqlQueryForFilmGenre = "DELETE FROM FILM_GENRE";
        jdbcTemplate.update(sqlQueryForFilmGenre);

        String sqlQueryForFilmLikes = "DELETE FROM FILM_LIKES";
        jdbcTemplate.update(sqlQueryForFilmLikes);
       return getAllFilms();
    }
    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        String sqlQueryForFilmLikes = "INSERT INTO FILM_LIKES VALUES (?, ?)";
        jdbcTemplate.update(sqlQueryForFilmLikes, filmId, userId);

        String sqlQueryForFilms = "UPDATE FILMS SET RATE = (SELECT COUNT(USER_ID) FROM FILM_LIKES WHERE FILM_ID = ?) " +
                "WHERE FILM_ID = ?;";
        jdbcTemplate.update(sqlQueryForFilms, filmId, filmId);
        return getFilmById(filmId);
    }
    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        String sqlQueryForFilmLikes = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQueryForFilmLikes, filmId, userId);

        String sqlQueryForFilms = "UPDATE FILMS SET RATE = (SELECT COUNT(USER_ID) FROM FILM_LIKES WHERE FILM_ID = ?) " +
                "WHERE FILM_ID = ?;";
        jdbcTemplate.update(sqlQueryForFilms, filmId, filmId);
        return getFilmById(filmId);
    }
    public Collection<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT * FROM FILMS_RATING_MPA_VIEW ORDER BY RATE DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }
    private Film deleteGenresByFilmId(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        return getFilmById(filmId);
    }
    private Film deleteLikesByFilmId(int filmId) {
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        return getFilmById(filmId);
    }
    private int saveFilmAndReturnId(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        return simpleJdbcInsert.executeAndReturnKey(film.toMap(film)).intValue();
    }
    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
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

        String sqlQueryForGenres = "SELECT * FROM FILMS_GENRES_VIEW WHERE FILM_ID = ?";
        if (film != null) {
            List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQueryForGenres, genreDbStorage::mapRowToGenre, film.getId()));
            if (genres.contains(null)) {
                genres = new ArrayList<>();
            }
            film.setGenres(genres);
        }


        String sqlQueryForLikes = "SELECT * FROM FILM_LIKES WHERE FILM_ID = ?";
        if (film != null) {
            Set<Integer> likes = new TreeSet<>(jdbcTemplate.query(sqlQueryForLikes, userDbStorage::mapRowToUserId, film.getId()));
            film.setLikes(likes);
        }
        return film;
    }
}