package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public Collection<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Genre getGenreById(int genreId) throws GenreNotFoundException {
        return genreDao.getGenreById(genreId);
    }
}