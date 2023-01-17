package ru.yandex.practicum.filmorate.storage.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
public class LikeDaoTest {
    @Autowired
    LikeDao likeDao;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addLike() {
        User testUser = likeDao.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        Film testFilm = likeDao.getFilmDbStorage().create(Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration((short) 100)
                .mpa(RatingMpa.builder()
                        .id(1).build())
                .rate(1)
                .likes(new TreeSet<>())
                .genres(new ArrayList<>())
                .build());

        Film foundFilm = likeDao.addLike(testFilm.getId(), testUser.getId());

        Set<Integer> likes = testFilm.getLikes();
        likes.add(testUser.getId());
        testFilm.setLikes(likes);
        testFilm.setRate(2);

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getPopularFilms() {
        User user = likeDao.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        Film film = likeDao.getFilmDbStorage().create(Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration((short) 100)
                .mpa(RatingMpa.builder()
                        .id(1).build())
                .rate(1)
                .likes(new TreeSet<>())
                .genres(new ArrayList<>())
                .build());

        likeDao.addLike(film.getId(), user.getId());
        Collection<Film> foundFilms = likeDao.getPopularFilms(10);

        Set<Integer> likes = film.getLikes();
        likes.add(user.getId());
        film.setLikes(likes);
        film.setRate(2);
        
        Collection<Film> testFilms = new ArrayList<>();
        testFilms.add(film);

        assertNotNull(foundFilms);
        assertEquals(testFilms, foundFilms);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteLike() {
        User user = likeDao.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        Set<Integer> likes = new TreeSet<>();
        likes.add(user.getId());

        Film testFilm = likeDao.getFilmDbStorage().create(Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration((short) 100)
                .mpa(RatingMpa.builder()
                        .id(1).build())
                .rate(2)
                .likes(likes)
                .genres(new ArrayList<>())
                .build());

        Film foundFilm = likeDao.deleteLike(testFilm.getId(), user.getId());

        testFilm.setRate(1);
        likes.clear();
        testFilm.setLikes(likes);

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }
}
