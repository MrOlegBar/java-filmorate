package ru.yandex.practicum.filmorate.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
public class FriendMapper {
    public static Integer mapRowToFriendId(ResultSet resultSet) throws SQLException {
        return  resultSet.getInt("friend_id");
    }
}