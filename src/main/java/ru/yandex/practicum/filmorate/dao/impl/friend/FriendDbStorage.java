package ru.yandex.practicum.filmorate.dao.impl.friend;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("friendDbStorage")
@Repository
@Slf4j
@AllArgsConstructor
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Integer mapRowToFriendId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }
}
