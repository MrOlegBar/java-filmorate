package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public Genre getGenreById(int genreId) {
        return genreDbStorage.getGenreById(genreId);
    }

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }
}