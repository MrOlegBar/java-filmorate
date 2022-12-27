package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

@Builder
public class FilmLike {
    private final int filmId;
    private final int userId;
}
