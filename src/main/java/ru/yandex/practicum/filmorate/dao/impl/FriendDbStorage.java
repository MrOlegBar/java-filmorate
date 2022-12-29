package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.impl.user.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

@Component("friendDbStorage")
@Repository
@Slf4j
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    public User addFriend(int userId, int friendId) throws UserNotFoundException {

        String sqlQueryForFilmLikes = "INSERT INTO USERS_FRIENDS VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQueryForFilmLikes, userId, friendId, false);


        return userDbStorage.getUserById(userId);
    }

    public User deleteFriend(int userId, int friendId) throws UserNotFoundException {
        return null;
    }

    public List<User> getFriends(int userId) throws UserNotFoundException {
        return null;
    }

    public List<User> getCorporateFriends(int userId, int otherUserId) throws UserNotFoundException {
        return null;
    }
}