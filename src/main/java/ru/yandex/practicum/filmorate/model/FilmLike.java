package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Builder
public class FilmLike {
    private final int filmId;
    private final int userId;
}
