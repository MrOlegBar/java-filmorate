package ru.yandex.practicum.filmorate.storage.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Repository
@Getter
@Slf4j
public class FriendDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FriendDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    public Collection<User> getAllFriends(int userId) throws UserNotFoundException {
        userDbStorage.getUserById(userId);

        String sqlQueryForFriends = "WITH friends_id AS" +
                "  (SELECT friend_id" +
                "   FROM users_friends" +
                "   WHERE user_id = ?)" +
                "SELECT u.user_id," +
                "       u.login," +
                "       u.name," +
                "       u.email," +
                "       u.birthday " +
                "FROM users AS u " +
                "JOIN friends_id AS f ON u.user_id = f.friend_id;";
        Collection<User> users = jdbcTemplate.query(sqlQueryForFriends, (resultSet, rowNum)
                -> UserMapper.mapRowToUser(resultSet), userId);

        users.forEach(user -> user.setFriends(userDbStorage.getFriends(user.getId())));
        return users;
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherUserId) throws UserNotFoundException {
        userDbStorage.getUserById(userId);
        userDbStorage.getUserById(otherUserId);

        String sqlQueryForFriends = "WITH user_friends AS" +
                "(SELECT friend_id AS user_friend_id " +
                "FROM users_friends " +
                "WHERE user_id = ?)," +
                "friend_friends AS" +
                "(SELECT friend_id AS friend_friend_id " +
                "FROM users_friends " +
                "WHERE user_id = ?)," +
                "common_friends AS" +
                "(SELECT user_friend_id AS common_friend_id " +
                "FROM user_friends " +
                "JOIN friend_friends ON user_friend_id = friend_friend_id)" +
                "SELECT u.user_id, u.login, u.name, u.email, u.birthday " +
                "FROM users AS u " +
                "JOIN common_friends ON u.user_id = common_friend_id";

        Collection<User> users = jdbcTemplate.query(sqlQueryForFriends, (resultSet, rowNum)
                -> UserMapper.mapRowToUser(resultSet), userId, otherUserId);

        users.forEach(user -> user.setFriends(userDbStorage.getFriends(user.getId())));
        return users;
    }

    /*String sqlQueryForFriends = String.format("WITH user_friends AS" +
                "(SELECT friend_id AS user_friend_id " +
                "FROM users_friends " +
                "WHERE user_id = %s)," +
                "friend_friends AS" +
                "(SELECT friend_id AS friend_friend_id " +
                "FROM users_friends " +
                "WHERE user_id = %s)," +
                "common_friends AS" +
                "(SELECT user_friend_id AS common_friend_id " +
                "FROM user_friends " +
                "JOIN friend_friends ON user_friend_id = friend_friend_id)" +
                "SELECT u.user_id, u.login, u.name, u.email, u.birthday " +
                "FROM users AS u " +
                "JOIN common_friends ON u.user_id = common_friend_id", userId, otherUserId);

        Collection<User> users = jdbcTemplate.query(sqlQueryForFriends, (resultSet, rowNum)
                -> UserMapper.mapRowToUser(resultSet));*/

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
            throw new IncorrectParameterException(String.format("Пользователь с friendId = %s уже добавлен в друзья, " +
                    "%s.", friendId, user));
        }

        if (friendFriendsFalse.contains(userId) || userFriendsFalse.contains(friendId)) {
            String sqlQueryForTrue = "MERGE INTO USERS_FRIENDS KEY (USER_ID, FRIEND_ID) VALUES (?, ?, true)";
            jdbcTemplate.update(sqlQueryForTrue, userId, friendId);
            jdbcTemplate.update(sqlQueryForTrue, friendId, userId);

            return userDbStorage.getUserById(userId);
        }

        String sqlQueryForFalse = "INSERT INTO USERS_FRIENDS VALUES (?, ?, false)";
        jdbcTemplate.update(sqlQueryForFalse, userId, friendId);

        User userWithFriend = userDbStorage.getUserById(userId);
        log.info("Добавлен друг с friendId = {} пользователю: {}", friendId, userWithFriend);
        return userWithFriend;
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

            User userWithoutFriend = userDbStorage.getUserById(userId);
            log.info("Удален друг с friendId = {} у пользователя: {}", friendId, userWithoutFriend);
            return userWithoutFriend;
        }

        User userWithoutFriend = userDbStorage.getUserById(userId);
        throw new IncorrectParameterException(String.format("Пользователь c friendId = %s отсутствует в списке друзей" +
                        " пользователя: %s.", friendId, userWithoutFriend));
    }
}