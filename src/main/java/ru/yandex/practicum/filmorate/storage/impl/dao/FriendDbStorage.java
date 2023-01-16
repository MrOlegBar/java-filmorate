package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Repository
@Getter
@Slf4j
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    public Collection<User> getAllFriends(int userId) throws UserNotFoundException {
        userDbStorage.getUserById(userId);

        String sqlQueryForFriends = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM USERS_FRIENDS " +
                "WHERE USER_ID = ?)";
        Collection<User> users = jdbcTemplate.query(sqlQueryForFriends, (resultSet, rowNum)
                -> UserMapper.mapRowToUser(resultSet), userId);

        users.forEach(user -> user.setFriends(userDbStorage.getFriends(user.getId())));
        return users;
    }

    public Collection<User> getCorporateFriends(int userId, int otherUserId) throws UserNotFoundException {
        userDbStorage.getUserById(userId);
        userDbStorage.getUserById(otherUserId);

        String sqlQueryForFriends = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM (SELECT USER_ID, " +
                "FRIEND_ID FROM USERS_FRIENDS WHERE USER_ID = ?) WHERE FRIEND_ID IN (SELECT FRIEND_ID " +
                "FROM USERS_FRIENDS WHERE USER_ID = ?))";

        Collection<User> users = jdbcTemplate.query(sqlQueryForFriends, (resultSet, rowNum)
                -> UserMapper.mapRowToUser(resultSet), userId, otherUserId);

        users.forEach(user -> user.setFriends(userDbStorage.getFriends(user.getId())));
        return users;
    }

    public User addFriend(int userId, int friendId) throws UserNotFoundException, IncorrectParameterException {
        User user = userDbStorage.getUserById(userId);
        User friend = userDbStorage.getUserById(friendId);

        Map<Boolean, Set<Integer>> userFriends = user.getFriends();
        Set<Integer> userFriendsFalse = userFriends.get(false);
        Set<Integer> userFriendsTrue = userFriends.get(true);

        Map<Boolean, Set<Integer>> friendFriends = friend.getFriends();
        Set<Integer> friendFriendsFalse = friendFriends.get(false);
        Set<Integer> friendFriendsTrue = friendFriends.get(true);

        if(friendFriendsTrue.contains(userId) || userFriendsTrue.contains(friendId)) {
            throw new IncorrectParameterException("Пользователь уже добавлен в друзья.");
        }

        if (friendFriendsFalse.contains(userId) || userFriendsFalse.contains(friendId)) {
            String sqlQueryForTrue = "MERGE INTO USERS_FRIENDS KEY(USER_ID, FRIEND_ID) VALUES (?, ?, true)";
            jdbcTemplate.update(sqlQueryForTrue, userId, friendId);
            jdbcTemplate.update(sqlQueryForTrue, friendId, userId);

            return userDbStorage.getUserById(userId);
        }

        String sqlQueryForFalse = "INSERT INTO USERS_FRIENDS VALUES (?, ?, false)";
        jdbcTemplate.update(sqlQueryForFalse, userId, friendId);

        log.info("Добавлен друг с friendId = {} пользователю с userId = {}", friendId, userId);
        return userDbStorage.getUserById(userId);
    }

    public User deleteFriend(int userId, int friendId) throws UserNotFoundException, IncorrectParameterException {
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