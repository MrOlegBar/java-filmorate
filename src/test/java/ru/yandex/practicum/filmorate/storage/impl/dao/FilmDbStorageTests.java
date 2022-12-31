package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmDbStorageTests {

    @Autowired
    @Qualifier("filmDbStorage")
    FilmStorage filmStorage;

    @Test
    public void contextLoads() {
    }

    @Test
    public void createFilm() {
        Film testFilm = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration((short) 100)
                .mpa(RatingMpa.builder()
                        .id(1).build())
                .rate(1)
                .likes(new TreeSet<>())
                .genres(new ArrayList<>())
                .build();

        Film film = filmStorage.createFilm(testFilm);
        Film foundFilm = filmStorage.getFilmById(film.getId());

        assertNotNull(foundFilm);
        assertEquals(film, foundFilm);
    }

    /*@Test
    public void getAllFilms() {
        Film film = filmStorage.getAllFilms();
        Film foundFilm = filmStorage.getFilmById(film.getId());

        assertNotNull(foundFilm);
        assertEquals(film, foundFilm);
    }*/


}
