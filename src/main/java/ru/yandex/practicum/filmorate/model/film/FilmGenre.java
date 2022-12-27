package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;

import javax.validation.constraints.Digits;

@Builder
public class FilmGenre {
    @Digits(integer = 2_147_483_647, fraction = 0)
    private final int filmId;
    @Digits(integer = 2_147_483_647, fraction = 0)
    private final int genreId;
}