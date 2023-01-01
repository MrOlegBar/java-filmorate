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
    FilmStorage filmStorage;

    @Test
    public void contextLoads() {
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

        Film foundFilm = filmStorage.create(testFilm);
        testFilm.setId(foundFilm.getId());
        testFilm.setMpa(RatingMpa.builder().id(1).name("G").build());

        assertNotNull(foundFilm);
        assertEquals(testFilm, foundFilm);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getAllFilms() {
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

        Film foundFilm = filmStorage.create(testFilm);
        testFilm.setId(foundFilm.getId());
        testFilm.setMpa(RatingMpa.builder().id(1).name("G").build());

        Collection<Film> films = new ArrayList<>();
        films.add(testFilm);

        Collection<Film> foundFilms = filmStorage.getAllFilms();

        assertNotNull(foundFilms);
        assertEquals(films, foundFilms);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getFilmById() {
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

        Film createdFilm = filmStorage.create(testFilm);

        testFilm.setId(createdFilm.getId());
        testFilm.setMpa(RatingMpa.builder().id(1).name("G").build());

        Film foundFilm = filmStorage.getFilmById(createdFilm.getId());

        assertNotNull(foundFilm);
        assertEquals(createdFilm, foundFilm);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void updateFilm() {
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

        Film createFilm = filmStorage.create(testFilm);

        Film foundFilm = filmStorage.update(Film.builder()
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

        Film updateFilm = filmStorage.getFilmById(foundFilm.getId());
        assertNotNull(foundFilm);
        assertEquals(updateFilm, foundFilm);
    }
}
