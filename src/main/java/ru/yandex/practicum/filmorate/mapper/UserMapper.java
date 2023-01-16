package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserMapper {
    public static User mapRowToUser(ResultSet resultSet) throws SQLException {

        return User.builder()
                .id(resultSet.getInt("user_id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate())
                .build();
    }
}