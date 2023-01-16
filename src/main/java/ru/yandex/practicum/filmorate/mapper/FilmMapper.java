package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class FilmMapper {
    public static Film mapRowToFilm(ResultSet resultSet) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseDate(Objects.requireNonNull(resultSet.getDate("release_date")).toLocalDate())
                .duration(resultSet.getShort("duration"))
                .mpa(RatingMpa.builder()
                        .id(resultSet.getInt("rating_MPA_id"))
                        .name(resultSet.getString("rating_MPA"))
                        .build())
                .rate(resultSet.getInt("rate"))
                .build();
    }
}