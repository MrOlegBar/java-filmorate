package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

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
    
    @DeleteMapping(value = { "/films", "/films/{filmId}"})
    public Collection<Film> deleteFilmS(@PathVariable(required = false) Integer filmId) throws FilmNotFoundException {
        if (filmId != null) {
            return filmService.deleteFilmById(filmId);
        } else {
            return filmService.deleteAllFilms();
        }
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count)
            throws FilmNotFoundException, UserNotFoundException, IncorrectParameterException {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film addLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws FilmNotFoundException, UserNotFoundException {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws FilmNotFoundException, UserNotFoundException {
        return filmService.deleteLike(filmId, userId);
    }
}