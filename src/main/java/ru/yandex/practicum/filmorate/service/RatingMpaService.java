package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.dao.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.exception.RatingMpaNotFoundException;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RatingMpaService {
    private final RatingMpaDbStorage ratingMpaStorage;

    public Collection<RatingMpa> getAllRatingsMpa() {
        return ratingMpaStorage.getAllRatingsMpa();
    }

    public RatingMpa getRatingMpaById(int ratingMpaId) throws RatingMpaNotFoundException {
        return ratingMpaStorage.getRatingMpaById(ratingMpaId);
    }
}