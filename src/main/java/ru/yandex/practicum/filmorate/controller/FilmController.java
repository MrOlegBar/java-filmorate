package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        return filmService.createFilm(film);
    }

    @GetMapping(value = { "/films", "/films/{filmId}"})
    //@ResponseStatus
    public Object getFilmS(@PathVariable(required = false) Integer filmId) throws FilmNotFoundException {
        if (filmId != null) {
            return filmService.getFilmById(filmId);
        } else {
            return filmService.getAllFilms();
        }
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody @Valid Film film) throws FilmNotFoundException {
        return filmService.updateFilm(film);
    }
}