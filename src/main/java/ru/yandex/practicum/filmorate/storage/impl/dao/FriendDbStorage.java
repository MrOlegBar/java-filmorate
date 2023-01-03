package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@Getter
@Slf4j
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MapRowToObject mapRowToObject;
    private final UserDbStorage userDbStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapRowToObject = new MapRowToObject(jdbcTemplate);
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    public List<User> getAllFriends(int userId) throws UserNotFoundException, FriendNotFoundException {
        userDbStorage.getUserById(userId);

        String sqlQueryForFriends = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM USERS_FRIENDS " +
                "WHERE USER_ID = ?)";

        return jdbcTemplate.query(sqlQueryForFriends, mapRowToObject::mapRowToUser, userId);
    }

    public List<User> getCorporateFriends(int userId, int otherUserId) throws UserNotFoundException
            , FriendNotFoundException {
        userDbStorage.getUserById(userId);
        userDbStorage.getUserById(otherUserId);

        String sqlQueryForFriends = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM (SELECT USER_ID, " +
                "FRIEND_ID FROM USERS_FRIENDS WHERE USER_ID = ?) WHERE FRIEND_ID IN (SELECT FRIEND_ID " +
                "FROM USERS_FRIENDS WHERE USER_ID = ?))";

        return jdbcTemplate.query(sqlQueryForFriends, mapRowToObject::mapRowToUser, userId, otherUserId);
    }

    public User addFriend(int userId, int friendId) throws UserNotFoundException, FriendNotFoundException
            , IncorrectParameterException {
        userDbStorage.getUserById(friendId);
        User user = userDbStorage.getUserById(userId);

        Map<Boolean, Set<Integer>> friends = user.getFriends();
        Set<Integer> friendsFalse = friends.get(false);
        Set<Integer> friendsTrue = friends.get(true);

        if(friendsTrue.contains(friendId)) {
            throw new IncorrectParameterException("Пользователь уже добавлен в друзья.");
        }

        if (friendsFalse.contains(friendId)) {
            String sqlQueryForTrue = "MERGE INTO USERS_FRIENDS KEY(USER_ID, FRIEND_ID) VALUES (?, ?, true)";
            jdbcTemplate.update(sqlQueryForTrue, userId, friendId);
            jdbcTemplate.update(sqlQueryForTrue, friendId, userId);

            return userDbStorage.getUserById(userId);
        }

        String sqlQueryForFalse = "INSERT INTO USERS_FRIENDS VALUES (?, ?, false)";
        jdbcTemplate.update(sqlQueryForFalse, userId, friendId);
        jdbcTemplate.update(sqlQueryForFalse, friendId, userId);
        log.info("Добавлен друг с id = {} пользователю с id = {}", friendId, userId);
        return userDbStorage.getUserById(userId);
    }

    public User deleteFriend(int userId, int friendId) throws UserNotFoundException, FriendNotFoundException
            , IncorrectParameterException {
        userDbStorage.getUserById(friendId);
        User user = userDbStorage.getUserById(userId);

        Map<Boolean, Set<Integer>> friends = user.getFriends();
        Set<Integer> friendsFalse = friends.get(false);
        Set<Integer> friendsTrue = friends.get(true);

        if (friendsTrue.contains(friendId)) {
            String sqlQuery = "MERGE INTO USERS_FRIENDS KEY (USER_ID, FRIEND_ID) VALUES (?, ?, false)";

            jdbcTemplate.update(sqlQuery, userId, friendId);
            jdbcTemplate.update(sqlQuery, friendId, userId);

            return userDbStorage.getUserById(userId);
        }

        if (friendsFalse.contains(friendId)) {
            String sqlQuery = "DELETE FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ? " +
                    "AND FRIENDSHIP_STATUS = false";

            jdbcTemplate.update(sqlQuery, userId, friendId);
            jdbcTemplate.update(sqlQuery, friendId, userId);

            log.info("Удален друг с id = {} пользователю с id = {}", friendId, userId);
            return userDbStorage.getUserById(userId);
        }
        throw new IncorrectParameterException("Пользователь отсутствует в списке друзей.");
    }
}