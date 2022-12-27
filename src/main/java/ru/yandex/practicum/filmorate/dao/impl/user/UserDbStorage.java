package ru.yandex.practicum.filmorate.dao.impl.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dao.impl.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("userDbStorage")
@Repository
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FriendDbStorage friendDbStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendDbStorage = new FriendDbStorage(jdbcTemplate);
    }


    @Override
    public User create(User user) {
        String validName = (user.getName().isBlank()) ? user.getLogin() : (user.getName());
        User validUser = User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(validName)
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
        
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        int userId = simpleJdbcInsert.executeAndReturnKey(validUser.toMap(validUser)).intValue();
        validUser.setId(userId);

        if (validUser.getFriends() != null) {
            Map<Boolean, Set<Integer>> friends = validUser.getFriends();

            for (Boolean status : friends.keySet()) {
                Set<Integer> friendsId = friends.get(status);
                for (Integer idFriend : friendsId) {
                    String sqlQueryForUserFriends = "INSERT INTO USER_FRIENDS(USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) " +
                            "VALUES (?, ?, ?)";
                    jdbcTemplate.update(sqlQueryForUserFriends,
                            validUser.getId(),
                            idFriend,
                            status);
                }
            }
        }
        return getUserById(userId);
    }
    @Override
    public Collection<User> findAll() {
        return null;
    }
    @Override
    public User update(User user) {
        return null;
    }
    @Override
    public User getUserById(int userId) {
        String sqlQueryForUsers = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQueryForUsers, this::mapRowToUser, userId);
    }
    public Collection<Integer> getAllLikes() {
        String sqlQuery = "SELECT * FROM FILM_LIKES ORDER BY FILM_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUserId);
    }

    public Integer mapRowToUserId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate())
                .build();

        Map<Boolean, Set<Integer>> friends = new TreeMap<>();

        if (user.getId() != 0) {
            String sqlQueryForStatusFalse = "SELECT * FROM USER_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = false";
            String sqlQueryForStatusTrue = "SELECT * FROM USER_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = true";

            Set<Integer> friendsForStatusFalse = new TreeSet<>(jdbcTemplate.query(sqlQueryForStatusFalse
                    , friendDbStorage::mapRowToFriendId, user.getId()));

            friends.put(false, friendsForStatusFalse);

            Set<Integer> friendsForStatusTrue = new TreeSet<>(jdbcTemplate.query(sqlQueryForStatusTrue
                    , friendDbStorage::mapRowToFriendId, user.getId()));

            friends.put(true, friendsForStatusTrue);
        }

        user.setFriends(friends);

        return user;
    }
}
