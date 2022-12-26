package ru.yandex.practicum.filmorate.dao.impl.ratingMpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.RatingMpaStorage;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

@Component("ratingMpaDbStorage")
@Repository
@Slf4j
@AllArgsConstructor
public class RatingMpaDbStorage implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public RatingMpa getRatingMpaById(int ratingMpaId) {
        String sqlQuery = "SELECT * FROM RATINGS_MPA WHERE RATING_MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRatingMpa, ratingMpaId);
    }

    @Override
    public Collection<RatingMpa> getAllRatingsMpa() {
        String sqlQuery = "SELECT * FROM RATINGS_MPA ORDER BY RATING_MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRatingMpa);
    }
    public RatingMpa mapRowToRatingMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return RatingMpa.builder()
                .id(resultSet.getInt("rating_mpa_id"))
                .name(resultSet.getString("rating_mpa"))
                .build();
    }
}