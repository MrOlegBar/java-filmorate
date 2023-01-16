package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
public class RatingMpaMapper {
    public static RatingMpa mapRowToRatingMpa(ResultSet resultSet) throws SQLException {
        return RatingMpa.builder()
                .id(resultSet.getInt("rating_mpa_id"))
                .name(resultSet.getString("rating_mpa"))
                .build();
    }
}