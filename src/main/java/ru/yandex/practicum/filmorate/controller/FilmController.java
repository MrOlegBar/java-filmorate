package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
public class FilmController {
    private final Map<Integer, Film> films = new TreeMap<>();
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film film) throws FilmNotFoundException {
        return filmService.update(film);
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable int filmId) throws FilmNotFoundException {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film addLike(@PathVariable("filmId") int filmId
            , @PathVariable("userId") int userId)
            throws FilmNotFoundException, UserNotFoundException {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable("filmId") int filmId
            , @PathVariable("userId") int userId)
            throws FilmNotFoundException, UserNotFoundException {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer countFilms)
            throws FilmNotFoundException, UserNotFoundException, IncorrectParameterException {
        if (countFilms <= 0) {
            throw new IncorrectParameterException("countFilms");
        }
        return filmService.getPopularFilms(countFilms);
    }
}