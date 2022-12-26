package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RatingMpaStorage;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RatingMpaService {
    private final RatingMpaStorage ratingMpaStorage;

    public RatingMpa getRatingMpaById(int ratingMpaId) {
        return ratingMpaStorage.getRatingMpaById(ratingMpaId);
    }

    public Collection<RatingMpa> getAllRatingsMpa() {
        return ratingMpaStorage.getAllRatingsMpa();
    }
}