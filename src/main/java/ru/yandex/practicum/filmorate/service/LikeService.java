package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeDbStorage likeDbStorage;

    public Collection<Film> getPopularFilms(Integer count) throws FilmNotFoundException {
        return likeDbStorage.getPopularFilms(count);
    }
    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        return likeDbStorage.addLike(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) throws UserNotFoundException, FilmNotFoundException
            , IncorrectParameterException {
        return likeDbStorage.deleteLike(filmId, userId);
    }

}
