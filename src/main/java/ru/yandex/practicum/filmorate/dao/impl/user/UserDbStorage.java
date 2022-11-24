package ru.yandex.practicum.filmorate.dao.impl.user;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("userDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT u.user_id,\n" +
                "\t\tu.login,\n" +
                "        u.name,\n" +
                "        u.email,\n" +
                "        u.birthday,\n" +
                "        array_agg(DISTINCT uf.friend_id) AS friends,\n" +
                "        fs.status\n" +
                "FROM users AS u\n" +
                "LEFT JOIN user_friends AS uf ON u.user_id=uf.user_id\n" +
                "LEFT JOIN users AS f ON uf.friend_id=f.user_id\n" +
                "LEFT JOIN friendship_status AS fs ON uf.friendship_status_id=fs.friendship_status_id\n" +
                "GROUP BY u.user_id, u.login, u.name, u.email, u.birthday, fs.status\n" +
                "ORDER BY u.user_id, fs.status DESC;";
        return jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return UserDbStorage.this.makeUser(rs);
            }
            }
            );
    }
    private User makeUser(ResultSet rs) throws SQLException {
        Map<String, List<Long>> friends = new HashMap<>();
        friends.put(rs.getString("STATUS"), rs.getObject("FRIENDS", List.class));
        if(rs.next()) {
        return new User(
                rs.getInt("USER_ID"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getDate("BIRTHDAY").toLocalDate(),
                friends);
        }
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
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT u.user_id,\n" +
                "\t\tu.login,\n" +
                "        u.name,\n" +
                "        u.email,\n" +
                "        u.birthday,\n" +
                "        array_agg(DISTINCT uf.friend_id) AS friends,\n" +
                "        fs.status\n" +
                "FROM users AS u\n" +
                "LEFT JOIN user_friends AS uf ON u.user_id=uf.user_id\n" +
                "LEFT JOIN users AS f ON uf.friend_id=f.user_id\n" +
                "LEFT JOIN friendship_status AS fs ON uf.friendship_status_id=fs.friendship_status_id\n" +
                "WHERE u.user_id = ?\n" +
                "GROUP BY u.user_id, u.login, u.name, u.email, u.birthday, fs.status\n" +
                "ORDER BY u.user_id, fs.status DESC;", userId);
        if(userRows.next()) {
            Map<String, List<Long>> friends = new HashMap<>();
            List<Long> numberFriends = userRows.getObject("friends", List.class);
            friends.put(userRows.getString("status"), numberFriends);
            return new User(
                    userRows.getInt("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getString("email"),
                    userRows.getDate("birthday").toLocalDate(),
                    friends
            );
        } else {
            return null;
        }
    }


}
