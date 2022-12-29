package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
@AllArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenreById(int genreId) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
    }

    public Collection<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public void deleteGenresByFilmId(int filmId) {
        String sqlQueryForSelect = "SELECT * FROM FILMS_GENRES_VIEW WHERE FILM_ID = ?";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQueryForSelect
                , GenreDbStorage.this::mapRowToGenre, filmId));

        if (!genres.isEmpty()){
            String sqlQueryForDelete = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQueryForDelete, filmId);
        }
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = null;
        if (resultSet.getInt("genre_id") != 0) {
            genre = Genre.builder()
                    .id(resultSet.getInt("genre_id"))
                    .name(resultSet.getString("genre"))
                    .build();
        }
        return genre;
    }
}