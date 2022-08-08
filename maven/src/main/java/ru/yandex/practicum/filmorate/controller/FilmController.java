package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    LocalDate dateCheck = LocalDate.of(1895, Calendar.DECEMBER, 28);

    @GetMapping
    public List<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        try {
            if (film.getReleaseDate().isBefore(dateCheck)) {
                throw new ValidationException("дата релиза — раньше 28 декабря 1895 года");
            }
            films.add(film);
        } catch (Exception e) {
            log.debug("Валидация не пройдена", e);
        }
        log.debug("Фильм: {} сохранен.", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        try {
            if (film.getReleaseDate().isBefore(dateCheck)) {
                throw new ValidationException("дата релиза — раньше 28 декабря 1895 года");
            }
            films.add(film);
        } catch (Exception e) {
            log.debug("Валидация не пройдена", e);
        }
        log.debug("Фильм: {} сохранен.", film);
        return film;
    }
}
