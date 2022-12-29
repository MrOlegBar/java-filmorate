package ru.yandex.practicum.filmorate.dao.impl.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("userDbStorage")
@Repository
@Slf4j
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String validName = user.getName().isBlank() ? user.getLogin() : user.getName();
        user.setName(validName);
        
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        int userId = simpleJdbcInsert.executeAndReturnKey(user.toMap(user)).intValue();
        user.setId(userId);

        Map<Boolean, Set<Integer>> friends = user.getFriends();

        if (friends != null) {

            for (Boolean status : friends.keySet()) {
                Set<Integer> friendsId = friends.get(status);
                for (Integer idFriend : friendsId) {
                    String sqlQueryForUserFriends = "INSERT INTO USERS_FRIENDS(USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) " +
                            "VALUES (?, ?, ?)";
                    jdbcTemplate.update(sqlQueryForUserFriends,
                            user.getId(),
                            idFriend,
                            status);
                }
            }
        }
        return getUserById(userId);
    }
    @Override
    public User update(User user) {
        String sqlQueryForUsers = "UPDATE USERS SET LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQueryForUsers
                , user.getLogin()
                , user.getName().isBlank() ? user.getLogin() : user.getName()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());

        deleteFriendsByUserId(user.getId());


        if (user.getFriends() != null) {
            Map<Boolean, Set<Integer>> friends = new TreeMap<>(user.getFriends());
            for (Boolean status : friends.keySet()) {
                Set<Integer> friendsIds = friends.get(status);
                for (Integer friendId : friendsIds) {
                    String sqlQueryForUserFriends = "MERGE INTO USERS_FRIENDS KEY (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) " +
                            "VALUES (?, ?, ?)";
                    jdbcTemplate.update(sqlQueryForUserFriends,
                            user.getId(),
                            friendId,
                            status);
                }
            }
        }

        return getUserById(user.getId());
    }
    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }
    @Override
    public User getUserById(int userId) {
        String sqlQueryForUsers = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQueryForUsers, this::mapRowToUser, userId);
    }

    public Integer mapRowToUserId(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("user_id");
    }

    public void deleteFriendsByUserId(int userId) {
        String sqlQueryForSelect = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS IN (false, true)";
        Set<Integer> select = new TreeSet<>(jdbcTemplate.query(sqlQueryForSelect
                , (resultSet, rowNum) -> UserDbStorage.this.mapRowToFriendId(resultSet), userId));

        if (!select.isEmpty()){
            String sqlQueryForDelete = "DELETE FROM USERS_FRIENDS WHERE USER_ID = ?";
            jdbcTemplate.update(sqlQueryForDelete, userId);
        }
    }

    public Integer mapRowToFriendId(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("friend_id");
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
            String sqlQueryForStatusFalse = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = false";
            String sqlQueryForStatusTrue = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = true";

            Set<Integer> friendsForStatusFalse = new TreeSet<>(jdbcTemplate.query(sqlQueryForStatusFalse
                    , (resultSetFalse, rowNumFalse) -> mapRowToFriendId(resultSet), user.getId()));

            friends.put(false, friendsForStatusFalse);

            Set<Integer> friendsForStatusTrue = new TreeSet<>(jdbcTemplate.query(sqlQueryForStatusTrue
                    , (resultSetTrue, rowNumTrue) -> mapRowToFriendId(resultSet), user.getId()));

            friends.put(true, friendsForStatusTrue);
        }

        user.setFriends(friends);

        return user;
    }
}
