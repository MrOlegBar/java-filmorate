package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeDao likeDao;

    public Collection<Film> getPopularFilms(Integer count) throws FilmNotFoundException {
        return likeDao.getPopularFilms(count);
    }
    public Film addLike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        return likeDao.addLike(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) throws UserNotFoundException, FilmNotFoundException
            , IncorrectParameterException {
        return likeDao.deleteLike(filmId, userId);
    }

}
