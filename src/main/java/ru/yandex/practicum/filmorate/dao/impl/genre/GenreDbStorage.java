package ru.yandex.practicum.filmorate.dao.impl.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("genreDbStorage")
@Repository
@Slf4j
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int genreId) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
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