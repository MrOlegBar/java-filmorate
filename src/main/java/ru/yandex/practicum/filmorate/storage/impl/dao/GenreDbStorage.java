package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.*;

@Repository
@Slf4j
@AllArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Collection<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        Collection<Genre> genres = jdbcTemplate.query(sqlQuery, (resultSet, rowNum)
                -> GenreMapper.mapRowToGenre(resultSet));

        if (genres.contains(null)) {
            genres.clear();
        }
        return genres;
    }

    public Genre getGenreById(int genreId) throws GenreNotFoundException {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        Genre genre;

        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum)
                    -> GenreMapper.mapRowToGenre(resultSet), genreId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Жанр с genreId = {} не найден.", genreId);
            throw new GenreNotFoundException(String.format("Жанр с genreId = %s не найден.", genreId));
        }
        return genre;
    }
}