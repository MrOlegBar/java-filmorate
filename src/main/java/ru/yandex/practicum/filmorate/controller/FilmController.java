package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new TreeMap<>();
    private final LocalDate dateCheck = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        releaseDateCheck(film.getReleaseDate());

        films.put(film.getId(), film);

        log.debug("Фильм: {} сохранен.", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) throws ValidationException {
        releaseDateCheck(film.getReleaseDate());

        if (!films.containsKey(film.getId()) || film.getId() < 1) {
            ValidationException e = new ValidationException("Film с id = " + film.getId() + " не существует");
            log.debug("Валидация не пройдена", e);
            throw e;
        }

        films.put(film.getId(), film);

        log.debug("Пользователь: {} сохранен.", film);
        return film;
    }

    public int getIdGeneration() {
        return idGeneration();
    }

    private int idGeneration() {
        int id = 1;
        while (films.containsKey(id)) {
            id++;
        }
        return id;
    }

    private void releaseDateCheck(LocalDate releaseDate) {
        if (releaseDate.isBefore(dateCheck)) {
            ValidationException e = new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
            log.debug("Валидация не пройдена", e);
            throw e;
        }
    }
}