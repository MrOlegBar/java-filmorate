package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.*;

@Repository
@Slf4j
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MapRowToObject mapRowToObject;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapRowToObject = new MapRowToObject(jdbcTemplate);
    }

    public Collection<Genre> getAllGenres() throws GenreNotFoundException {
        String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sqlQuery, mapRowToObject::mapRowToGenre);
    }

    public Genre getGenreById(int genreId) throws GenreNotFoundException {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, mapRowToObject::mapRowToGenre, genreId);
    }
}