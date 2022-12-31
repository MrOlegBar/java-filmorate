package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
@AllArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Collection<Genre> getAllGenres() throws GenreNotFoundException {
        String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre getGenreById(int genreId) throws GenreNotFoundException {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
    }

    public void deleteGenresByFilmId(int filmId) throws GenreNotFoundException {
        String sqlQuery = "SELECT * FROM FILMS_GENRES_VIEW WHERE FILM_ID = ?";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQuery, GenreDbStorage.this::mapRowToGenre, filmId));

        if (!genres.isEmpty()){
            String sqlQueryForDeleteGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryForDeleteGenres, filmId);
        }
        log.info("Удалены жанры у фильма с id = {}", filmId);
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException, GenreNotFoundException {
        Genre genre;
        int genreId = resultSet.getInt("genre_id");

        if (genreId >= 0) {
            genre = Genre.builder()
                    .id(genreId)
                    .name(resultSet.getString("genre"))
                    .build();
        } else {
            log.error("Жанр с id = {} не существует.", genreId);
            throw new GenreNotFoundException(String.format("Жанр с id = %s не существует.", genreId));
        }
        return genre;
    }
}