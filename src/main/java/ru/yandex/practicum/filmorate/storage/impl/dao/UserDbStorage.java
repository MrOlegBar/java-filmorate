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
    public User create(User user) throws UserNotFoundException, FriendNotFoundException {
        String validName = user.getName().isBlank() ? user.getLogin() : user.getName();
        user.setName(validName);
        
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        int userId = simpleJdbcInsert.executeAndReturnKey(user.toMap(user)).intValue();

        Map<Boolean, Set<Integer>> friends = user.getFriends();

        if (friends != null) {

            for (Boolean status : friends.keySet()) {
                Set<Integer> friendsId = friends.get(status);
                for (Integer idFriend : friendsId) {
                    String sqlQuery = "INSERT INTO USERS_FRIENDS(USER_ID, FRIEND_ID, FRIENDSHIP_STATUS)" +
                            " VALUES (?, ?, ?)";
                    jdbcTemplate.update(sqlQuery, user.getId(), idFriend, status);
                }
            }
        }

        log.info("Создан пользователь: {}", getUserById(userId));

        return getUserById(userId);
    }
    @Override
    public Collection<User> getAllUsers() throws UserNotFoundException, FriendNotFoundException {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, mapRowToObject::mapRowToUser);
    }
    @Override
    public User getUserById(int userId) throws UserNotFoundException, FriendNotFoundException {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, mapRowToObject::mapRowToUser, userId);
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

        if (user.getFriends() != null) {
            Map<Boolean, Set<Integer>> friends = new TreeMap<>(user.getFriends());
            for (Boolean status : friends.keySet()) {
                Set<Integer> friendsIds = friends.get(status);
                for (Integer friendId : friendsIds) {
                    String sqlQueryForUpdateFriends = "MERGE INTO USERS_FRIENDS KEY (USER_ID, FRIEND_ID" +
                            ", FRIENDSHIP_STATUS) VALUES (?, ?, ?)";
                    jdbcTemplate.update(sqlQueryForUpdateFriends, user.getId(), friendId, status);
                }
            }
        }

        log.info("Пользователь с id = {} обновлен.", user.getId());
        return getUserById(user.getId());
    }

    public void deleteFriendsByUserId(int userId) throws FriendNotFoundException {
        String sqlQuery = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS IN (false, true)";
        Set<Integer> friends = new TreeSet<>(jdbcTemplate.query(sqlQuery, mapRowToObject::mapRowToFriendId
                , userId));

        if (!friends.isEmpty()){
            String sqlQueryForDeleteFriends = "DELETE FROM USERS_FRIENDS WHERE USER_ID = ?";
            jdbcTemplate.update(sqlQueryForDeleteFriends, userId);
        }
        log.info("Удалены друзья у пользователя с id = {}", userId);
    }
}
