package ru.yandex.practicum.filmorate.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
public class LikeMapper {
    public static Integer mapRowToUserId(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("user_id");
    }
}