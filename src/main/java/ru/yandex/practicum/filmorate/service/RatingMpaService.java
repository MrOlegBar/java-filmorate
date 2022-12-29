package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RatingMpaService {
    private final RatingMpaDbStorage ratingMpaStorage;

    public RatingMpa getRatingMpaById(int ratingMpaId) {
        return ratingMpaStorage.getRatingMpaById(ratingMpaId);
    }

    public Collection<RatingMpa> getAllRatingsMpa() {
        return ratingMpaStorage.getAllRatingsMpa();
    }
}