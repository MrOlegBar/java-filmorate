package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new TreeMap<>();
    private final LocalDate dateCheck = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.update(film);
    }
}