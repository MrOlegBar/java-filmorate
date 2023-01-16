package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenreById(int genreId) throws GenreNotFoundException {
        return genreDbStorage.getGenreById(genreId);
    }
}