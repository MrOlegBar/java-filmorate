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
    public void contextLoads() {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addLike() {
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

        User testUser = likeDbStorage.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        Set<Integer> likes = testFilm.getLikes();
        likes.add(testUser.getId());
        testFilm.setLikes(likes);
        testFilm.setRate(2);

        Film foundFilm = likeDbStorage.addLike(testFilm.getId(), testUser.getId());

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }
}
