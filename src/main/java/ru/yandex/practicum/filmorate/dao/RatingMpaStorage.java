package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.util.Collection;

public interface RatingMpaStorage {
    RatingMpa getRatingMpaById(int ratingMpaId);

    Collection<RatingMpa> getAllRatingsMpa();
}