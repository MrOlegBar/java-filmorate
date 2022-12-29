package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeDbStorage likeDbStorage;

    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        return likeDbStorage.addLike(filmId, userId);
    }
    public Collection<Film> getPopularFilms(Integer count) {
        return likeDbStorage.getPopularFilms(count);
    }

    public Film deleteLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        return likeDbStorage.deleteLike(filmId, userId);
    }

}
