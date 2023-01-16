package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RatingMpaNotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMpaMapper;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.util.Collection;

@Repository
@Slf4j
@AllArgsConstructor
public class RatingMpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Collection<RatingMpa> getAllRatingsMpa() {
        String sqlQuery = "SELECT * FROM RATINGS_MPA ORDER BY RATING_MPA_ID";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> RatingMpaMapper.mapRowToRatingMpa(resultSet));
    }

    public RatingMpa getRatingMpaById(int ratingMpaId) throws RatingMpaNotFoundException {
        String sqlQuery = "SELECT * FROM RATINGS_MPA WHERE RATING_MPA_ID = ?";
        RatingMpa ratingMpa;

        try {
            ratingMpa = jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum)
                    -> RatingMpaMapper.mapRowToRatingMpa(resultSet), ratingMpaId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Рейтинг MPA с ratingMpaId = {} не найден.", ratingMpaId);
            throw new RatingMpaNotFoundException(String.format("Рейтинг MPA с ratingMpaId = %s не найден."
                    , ratingMpaId));
        }

        return ratingMpa;
    }
}