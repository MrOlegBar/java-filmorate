package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.RatingMpaDao;
import ru.yandex.practicum.filmorate.exception.RatingMpaNotFoundException;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RatingMpaService {
    private final RatingMpaDao ratingMpaDao;

    public Collection<RatingMpa> getAllRatingsMpa() {
        return ratingMpaDao.getAllRatingsMpa();
    }

    public RatingMpa getRatingMpaById(int ratingMpaId) throws RatingMpaNotFoundException {
        return ratingMpaDao.getRatingMpaById(ratingMpaId);
    }
}