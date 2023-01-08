package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmDbStorageTests {

    @Autowired
    @Qualifier("filmDbStorage")
    FilmStorage filmDbStorage;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void createFilm() {
        Film testFilm = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration((short) 100)
                .mpa(RatingMpa.builder().id(1).build())
                .rate(1)
                .likes(new TreeSet<>())
                .genres(new ArrayList<>())
                .build();

        Film foundFilm = filmDbStorage.create(testFilm);
        testFilm.setId(foundFilm.getId());
        testFilm.setMpa(RatingMpa.builder().id(1).name("G").build());

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllFilms() {
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

        Film foundFilm = filmDbStorage.create(testFilm);
        testFilm.setId(foundFilm.getId());
        testFilm.setMpa(RatingMpa.builder().id(1).name("G").build());

        Collection<Film> testFilms = new ArrayList<>();
        testFilms.add(testFilm);

        Collection<Film> foundFilms = filmDbStorage.getAllFilms();

        assertNotNull(foundFilms);
        assertEquals(testFilms, foundFilms);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getFilmById() {
        Film film = Film.builder()
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

        Film testFilm = filmDbStorage.create(film);

        film.setId(testFilm.getId());
        film.setMpa(RatingMpa.builder().id(1).name("G").build());

        Film foundFilm = filmDbStorage.getFilmById(testFilm.getId());

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateFilm() {
        Film film = Film.builder()
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

        Film createFilm = filmDbStorage.create(film);

        Film foundFilm = filmDbStorage.update(Film.builder()
                .id(createFilm.getId())
                .name("New name")
                .description("New description")
                .releaseDate(LocalDate.parse("1967-10-25"))
                .duration((short) 150)
                .mpa(RatingMpa.builder()
                        .id(2).build())
                .rate(2)
                .likes(new TreeSet<>())
                .genres(new ArrayList<>())
                .build());

        Film testFilm = filmDbStorage.getFilmById(foundFilm.getId());

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }
}
