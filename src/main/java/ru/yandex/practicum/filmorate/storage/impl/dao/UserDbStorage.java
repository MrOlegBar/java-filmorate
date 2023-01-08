package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.util.*;

@Component("userDbStorage")
@Repository
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MapRowToObject mapRowToObject;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapRowToObject = new MapRowToObject(jdbcTemplate);
    }

    @Override
    @Transactional
    public User create(User user) throws UserNotFoundException, FriendNotFoundException {
        String validName = user.getName().isBlank() ? user.getLogin() : user.getName();
        user.setName(validName);
        
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        int userId = simpleJdbcInsert.executeAndReturnKey(user.toMap(user)).intValue();
        Map<Boolean, Set<Integer>> friends = user.getFriends();

        if (friends != null) {
            friends.forEach((status, friendsId) -> jdbcTemplate.batchUpdate(
                    "INSERT INTO USERS_FRIENDS(USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) VALUES (?, ?, ?)",
                    friendsId,
                    friendsId.size(),
                    (PreparedStatement ps, Integer idFriend) -> {
                        ps.setInt(1, userId);
                        ps.setInt(2, idFriend);
                        ps.setBoolean(3, status);
                    }));
        }

        log.info("Создан пользователь: {}", getUserById(userId));

        return getUserById(userId);
    }
    @Override
    public Collection<User> getAllUsers() throws UserNotFoundException, FriendNotFoundException {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> mapRowToObject.mapRowToUser(resultSet));
    }
    @Override
    public User getUserById(int userId) throws UserNotFoundException, FriendNotFoundException {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowNum)
                -> mapRowToObject.mapRowToUser(resultSet), userId);
    }
    @Override
    public User update(User user) throws UserNotFoundException, FriendNotFoundException {
        getUserById(user.getId());
        if (user.getId() < 1) {
            log.error("Пользователь с id = {} не найден.", user.getId());
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден.", user.getId()));
        }

        String sqlQuery = "UPDATE USERS SET LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getLogin(), user.getName().isBlank() ? user.getLogin() : user.getName()
                , user.getEmail(), user.getBirthday() , user.getId());

        deleteFriendsByUserId(user.getId());

        Map<Boolean, Set<Integer>> friends = user.getFriends();
        if (friends != null) {
            friends.forEach((status, friendsId) -> jdbcTemplate.batchUpdate(
                    "MERGE INTO USERS_FRIENDS KEY (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) VALUES (?, ?, ?)",
                    friendsId,
                    friendsId.size(),
                    (PreparedStatement ps, Integer idFriend) -> {
                        ps.setInt(1, user.getId());
                        ps.setInt(2, idFriend);
                        ps.setBoolean(3, status);
                    }));
        }

        log.info("Пользователь с id = {} обновлен.", user.getId());
        return getUserById(user.getId());
    }

    public void deleteFriendsByUserId(int userId) throws FriendNotFoundException {
        String sqlQuery = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS IN (false, true)";
        Set<Integer> friends = new TreeSet<>(jdbcTemplate.query(sqlQuery, (resultSet, rowNumFalse)
                        -> mapRowToObject.mapRowToFriendId(resultSet)
                , userId));

        if (!friends.isEmpty()){
            String sqlQueryForDeleteFriends = "DELETE FROM USERS_FRIENDS WHERE USER_ID = ?";
            jdbcTemplate.update(sqlQueryForDeleteFriends, userId);
        }
        log.info("Удалены друзья у пользователя с id = {}", userId);
    }
}
