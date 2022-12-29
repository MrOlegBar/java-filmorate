package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film putLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws FilmNotFoundException, UserNotFoundException {
        return likeService.addLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count)
            throws FilmNotFoundException, UserNotFoundException, IncorrectParameterException {
        if (count <= 0) {
            throw new IncorrectParameterException(String.format("Параметр метода должен быть > 0,count=%s", count));
        }
        return likeService.getPopularFilms(count);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws FilmNotFoundException, UserNotFoundException {
        return likeService.deleteLike(filmId, userId);
    }
}
