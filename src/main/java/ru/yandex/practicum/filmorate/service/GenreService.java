package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreDbStorage;

    public Genre getGenreById(int genreId) {
        return genreDbStorage.getGenreById(genreId);
    }

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }
}