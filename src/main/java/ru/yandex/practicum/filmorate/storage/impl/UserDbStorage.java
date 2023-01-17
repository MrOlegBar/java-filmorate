package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FriendMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.*;

@Component("userDbStorage")
@Repository
@Slf4j
@AllArgsConstructor
public class UserDbStorage implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) throws UserNotFoundException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        int userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();

        User createdUser = getUserById(userId);
        log.info("Создан пользователь: {}.", createdUser);
        return createdUser;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM USERS";

        Collection<User> users = jdbcTemplate.query(sqlQuery, (resultSet, rowNum)
                -> UserMapper.mapRowToUser(resultSet));

        users.forEach(user -> user.setFriends(getFriends(user.getId())));
        return users;
    }

    @Override
    public User getUserById(int userId) throws UserNotFoundException {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        User user;

        try {
            user = jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum) -> UserMapper.mapRowToUser(resultSet), userId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Пользователь с userId = {} не найден.", userId);
            throw new UserNotFoundException(String.format("Пользователь с userId = %s не найден.", userId));
        }

        if (user != null) {
            user.setFriends(getFriends(userId));
        }
        return user;
    }

    @Override
    public User update(User user) throws UserNotFoundException {
        getUserById(user.getId());

        String sqlQuery = "UPDATE USERS SET LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getLogin(), user.getName().isBlank() ? user.getLogin() : user.getName(),
                user.getEmail(), user.getBirthday() , user.getId());

        User updatedUser = getUserById(user.getId());

        log.info("Обновлен пользователь: {}.", updatedUser);
        return updatedUser;
    }

    public Map<Boolean, Set<Integer>> getFriends(int userId) throws UserNotFoundException {
        Map<Boolean, Set<Integer>> friends = new TreeMap<>();

        String sqlQuery = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = false";
        String sqlQueryForStatusTrue = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = true";

        Set<Integer> friendsForStatusFalse = new TreeSet<>(jdbcTemplate.query(sqlQuery, (resultSet1, rowNumFalse)
                -> FriendMapper.mapRowToFriendId(resultSet1), userId));

        friends.put(false, friendsForStatusFalse);

        Set<Integer> friendsForStatusTrue = new TreeSet<>(jdbcTemplate.query(sqlQueryForStatusTrue,
                (resultSet1, rowNumFalse) -> FriendMapper.mapRowToFriendId(resultSet1), userId));

        friends.put(true, friendsForStatusTrue);
        return friends;
    }
}