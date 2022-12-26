package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.service.GenreService;

@RestController
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping(value = { "/genres", "/genres/{genreId}"})
    @ResponseBody
    public Object getGenreS(@PathVariable(required = false) Integer genreId) throws GenreNotFoundException {
        if (genreId != null) {
            return genreService.getGenreById(genreId);
        } else {
            return genreService.getAllGenres();
        }
    }
}