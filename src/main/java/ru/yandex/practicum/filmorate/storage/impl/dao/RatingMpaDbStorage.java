package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RatingMpaNotFoundException;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.util.Collection;

@Repository
@Slf4j
public class RatingMpaDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MapRowToObject mapRowToObject;

    public RatingMpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapRowToObject = new MapRowToObject(jdbcTemplate);
    }

    public Collection<RatingMpa> getAllRatingsMpa() throws RatingMpaNotFoundException {
        String sqlQuery = "SELECT * FROM RATINGS_MPA ORDER BY RATING_MPA_ID";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> mapRowToObject.mapRowToRatingMpa(resultSet));
    }

    public RatingMpa getRatingMpaById(int ratingMpaId) throws RatingMpaNotFoundException {
        String sqlQuery = "SELECT * FROM RATINGS_MPA WHERE RATING_MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum) -> mapRowToObject.mapRowToRatingMpa(resultSet), ratingMpaId);
    }
}