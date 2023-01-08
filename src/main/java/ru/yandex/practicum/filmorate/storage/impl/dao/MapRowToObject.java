package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class MapRowToObject {

    private final JdbcTemplate jdbcTemplate;

    public MapRowToObject(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film mapRowToFilm(ResultSet resultSet) throws SQLException, FilmNotFoundException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseDate(Objects.requireNonNull(resultSet.getDate("release_date")).toLocalDate())
                .duration(resultSet.getShort("duration"))
                .mpa(RatingMpa.builder()
                        .id(resultSet.getInt("rating_MPA_id"))
                        .name(resultSet.getString("rating_MPA"))
                        .build())
                .rate(resultSet.getInt("rate"))
                .genres(getGenres(resultSet.getInt("film_id")))
                .likes(getLikes(resultSet.getInt("film_id")))
                .build();
    }
    public Genre mapRowToGenre(ResultSet resultSet) throws SQLException, GenreNotFoundException {
        Genre genre;
        int genreId = resultSet.getInt("genre_id");

        if (genreId >= 0) {
            genre = Genre.builder()
                    .id(genreId)
                    .name(resultSet.getString("genre"))
                    .build();
        } else {
            log.error("Жанр с id = {} не существует.", genreId);
            throw new GenreNotFoundException(String.format("Жанр с id = %s не существует.", genreId));
        }
        return genre;
    }
    public Integer mapRowToUserId(ResultSet resultSet) throws SQLException, UserNotFoundException {
        int userId =  resultSet.getInt("user_id");
        if (userId > 0) {
            return userId;
        } else {
            log.error("Пользователь с id = {} не существует.", userId);
            throw new UserNotFoundException(String.format("Пользователь с id = %s не существует.", userId));
        }
    }
    public Integer mapRowToFriendId(ResultSet resultSet) throws SQLException, FriendNotFoundException {
        int friendId =  resultSet.getInt("friend_id");
        if (friendId > 0) {
            return friendId;
        } else {
            log.error("Друг с id = {} не существует.", friendId);
            throw new FriendNotFoundException(String.format("Друг с id = %s не существует.", friendId));
        }
    }
    public User mapRowToUser(ResultSet resultSet) throws SQLException, UserNotFoundException
            , FriendNotFoundException {
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate())
                .build();

        if (user.getId() > 0) {
            Map<Boolean, Set<Integer>> friends = new TreeMap<>();

            String sqlQuery = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = false";
            String sqlQueryForStatusTrue = "SELECT * FROM USERS_FRIENDS WHERE USER_ID = ? AND FRIENDSHIP_STATUS = true";

            Set<Integer> friendsForStatusFalse = new TreeSet<>(jdbcTemplate.query(sqlQuery
                    , (resultSet1, rowNumFalse) -> MapRowToObject.this.mapRowToFriendId(resultSet1), user.getId()));

            friends.put(false, friendsForStatusFalse);

            Set<Integer> friendsForStatusTrue = new TreeSet<>(jdbcTemplate.query(sqlQueryForStatusTrue
                    , (resultSet1, rowNumFalse) -> MapRowToObject.this.mapRowToFriendId(resultSet1), user.getId()));

            friends.put(true, friendsForStatusTrue);

            user.setFriends(friends);
        } else {
            log.error("Пользователь с id = {} не найден.", user.getId());
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден.", user.getId()));
        }
        return user;
    }
    public RatingMpa mapRowToRatingMpa(ResultSet resultSet) throws SQLException
            , RatingMpaNotFoundException {
        RatingMpa ratingMpa;
        int ratingMpaId = resultSet.getInt("rating_mpa_id");

        if (ratingMpaId >= 0) {
            ratingMpa = RatingMpa.builder()
                    .id(ratingMpaId)
                    .name(resultSet.getString("rating_mpa"))
                    .build();
        } else {
            log.error("Рейтинг фильма с id = {} не существует.", ratingMpaId);
            throw new RatingMpaNotFoundException(String.format("Рейтинг фильма с id = %s не существует.", ratingMpaId));
        }
        return ratingMpa;
    }
    private List<Genre> getGenres (int filmId) {
        if (filmId > 0) {
            String sqlQueryForGenres = "SELECT * FROM FILMS_GENRES_VIEW WHERE FILM_ID = ?";

            List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQueryForGenres
                    , (resultSet1, rowNum) -> MapRowToObject.this.mapRowToGenre(resultSet1), filmId));

            if (genres.contains(Genre.builder().id(0).name(null).build())) {
                genres.clear();
            }

            return genres;
        } else {
            log.error("Фильм с id = {} не найден.", filmId);
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
    }

    private Set<Integer> getLikes(int filmId) {
        if (filmId > 0) {

            String sqlQueryForLikes = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ?";

            return new TreeSet<>(jdbcTemplate.query(sqlQueryForLikes, (resultSetLikes, rowNumLikes)
                    -> this.mapRowToUserId(resultSetLikes), filmId));
        } else {
            log.error("Фильм с id = {} не найден.", filmId);
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", filmId));
        }
    }
}
