package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
public class GenreDbStorageTest {

    @Autowired
    GenreDbStorage genreDbStorage;

    @Test
    void contextLoads() {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllGenres() {
        Collection<Genre> testGenres = new ArrayList<>();

        testGenres.add(Genre.builder().id(1).name("Комедия").build());
        testGenres.add(Genre.builder().id(2).name("Драма").build());
        testGenres.add(Genre.builder().id(3).name("Мультфильм").build());
        testGenres.add(Genre.builder().id(4).name("Триллер").build());
        testGenres.add(Genre.builder().id(5).name("Документальный").build());
        testGenres.add(Genre.builder().id(6).name("Боевик").build());

        Collection<Genre> foundGenres = genreDbStorage.getAllGenres();

        assertNotNull(foundGenres);
        assertEquals(testGenres, foundGenres);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getGenreById() {
        Genre testGenre = Genre.builder().id(1).name("Комедия").build();

        Genre foundGenre = genreDbStorage.getGenreById(1);

        assertNotNull(foundGenre);
        assertEquals(testGenre, foundGenre);
    }
}
