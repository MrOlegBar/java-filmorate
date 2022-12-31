package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.RatingMpaNotFoundException;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@Slf4j
@AllArgsConstructor
public class RatingMpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Collection<RatingMpa> getAllRatingsMpa() throws RatingMpaNotFoundException {
        String sqlQuery = "SELECT * FROM RATINGS_MPA ORDER BY RATING_MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRatingMpa);
    }

    public RatingMpa getRatingMpaById(int ratingMpaId) throws RatingMpaNotFoundException {
        String sqlQuery = "SELECT * FROM RATINGS_MPA WHERE RATING_MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRatingMpa, ratingMpaId);
    }
    public RatingMpa mapRowToRatingMpa(ResultSet resultSet, int rowNum) throws SQLException
            , RatingMpaNotFoundException {
        RatingMpa ratingMpa;
        int ratingMpaId = resultSet.getInt("rating_mpa_id");

        if (ratingMpaId >= 0) {
            ratingMpa = RatingMpa.builder()
                    .id(ratingMpaId)
                    .name(resultSet.getString("rating_mpa"))
                    .build();
        } else {
            log.error("Рейтинг фильма с id = {} не существует.", ratingMpaId);
            throw new RatingMpaNotFoundException(String.format("Рейтинг фильма с id = %s не существует.", ratingMpaId));
        }
        return ratingMpa;
    }
}