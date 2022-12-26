package ru.yandex.practicum.filmorate.dao.impl.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("userDbStorage")
@Repository
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }
    @Override
    public User create(User user) {
        return null;
    }
    @Override
    public User update(User user) {
        return null;
    }
    @Override
    public User getUserById(int userId) {
        return null;
    }
    public Collection<Integer> getAllLikes() {
        String sqlQuery = "SELECT * FROM FILM_LIKES ORDER BY FILM_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUserId);
    }

    public Integer mapRowToUserId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }
}
