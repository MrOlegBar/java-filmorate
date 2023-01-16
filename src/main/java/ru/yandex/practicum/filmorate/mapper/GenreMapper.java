package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
public class GenreMapper {
    public static Genre mapRowToGenre(ResultSet resultSet) throws SQLException {
        int genreId = resultSet.getInt("genre_id");

        if (genreId == 0) {
            return null;
        } else {
            return Genre.builder()
                    .id(genreId)
                    .name(resultSet.getString("genre"))
                    .build();
        }
    }
}