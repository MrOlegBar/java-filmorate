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
    public Film postFilm(@Valid @RequestBody Film film) throws ValidationException, FilmNotFoundException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должны быть не раньше 28.12.1895 г.");
        }
        return filmService.create(film);
    }

    @GetMapping(value = { "/films", "/films/{filmId}"})
    public Object getFilmS(@PathVariable(required = false) Integer filmId) throws FilmNotFoundException {
        if (filmId == null) {
            return filmService.getAllFilms();
        } else {
            return filmService.getFilmById(filmId);
        }
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody @Valid Film film) throws FilmNotFoundException, UserNotFoundException {
        return filmService.update(film);
    }
}