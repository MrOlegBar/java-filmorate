package ru.yandex.practicum.filmorate.dao.impl.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.user.UserDbStorage;
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
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap(film)).intValue());

        if (film.getGenres() != null) {
            String sqlQueryForFilmGenre = "INSERT INTO FILMS_GENRES(FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQueryForFilmGenre,
                        film.getId(),
                        genre.getId());
            }
        }

        if (film.getLikes() != null) {
            String sqlQueryForFilmLikes = "INSERT INTO FILMS_LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
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
        String sqlQueryForFilms = "SELECT * FROM FILMS_RATINGS_MPA_VIEW WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQueryForFilms, this::mapRowToFilm, filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM FILMS_RATINGS_MPA_VIEW";
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

       genreDbStorage.deleteGenresByFilmId(film.getId());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryForGenres = "MERGE INTO FILMS_GENRES KEY (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQueryForGenres,
                        film.getId(),
                        genre.getId());
            }
        }

        deleteLikesByFilmId(film.getId());

        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                String sqlQueryForGenres = "MERGE INTO FILMS_LIKES KEY (FILM_ID, USER_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQueryForGenres,
                        film.getId(),
                        userId);
            }
        }

        return getFilmById(film.getId());
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
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

        if (film.getId() != 0) {
            String sqlQueryForGenres = "SELECT * FROM FILMS_GENRES_VIEW WHERE FILM_ID = ?";

            List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQueryForGenres, genreDbStorage::mapRowToGenre, film.getId()));
            if (genres.contains(null)) {
                genres = new ArrayList<>();
            }
            film.setGenres(genres);
        }

        if (film.getId() != 0) {
            String sqlQueryForLikes = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ?";

            Set<Integer> likes = new TreeSet<>(jdbcTemplate.query(sqlQueryForLikes, (resultSetLikes, rowNumLikes) -> userDbStorage.mapRowToUserId(resultSetLikes), film.getId()));
            film.setLikes(likes);
        }
        return film;
    }
    private void deleteLikesByFilmId(int filmId) {
        String sqlQueryForSelect = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ?";
        Set<Integer> likes = new TreeSet<>(jdbcTemplate.query(sqlQueryForSelect
                , (resultSet, rowNum) -> userDbStorage.mapRowToUserId(resultSet), filmId));

        if (!likes.isEmpty()){
            String sqlQueryForDelete = "DELETE FROM FILMS_LIKES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryForDelete, filmId);
        }
    }
}