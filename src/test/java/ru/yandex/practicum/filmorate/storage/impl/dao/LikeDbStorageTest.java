package ru.yandex.practicum.filmorate.storage.impl.dao;

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
public class LikeDbStorageTest {
    @Autowired
    LikeDbStorage likeDbStorage;

    @Test
    void contextLoads() {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addLike() {
        User testUser = likeDbStorage.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        Film testFilm = likeDbStorage.getFilmDbStorage().create(Film.builder()
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

        Film foundFilm = likeDbStorage.addLike(testFilm.getId(), testUser.getId());

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
        User user = likeDbStorage.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        Film film = likeDbStorage.getFilmDbStorage().create(Film.builder()
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

        likeDbStorage.addLike(film.getId(), user.getId());
        Collection<Film> foundFilms = likeDbStorage.getPopularFilms(10);

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
        User user = likeDbStorage.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        Set<Integer> likes = new TreeSet<>();
        likes.add(user.getId());

        Film testFilm = likeDbStorage.getFilmDbStorage().create(Film.builder()
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

        Film foundFilm = likeDbStorage.deleteLike(testFilm.getId(), user.getId());

        testFilm.setRate(1);
        likes.clear();
        testFilm.setLikes(likes);

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }
}
