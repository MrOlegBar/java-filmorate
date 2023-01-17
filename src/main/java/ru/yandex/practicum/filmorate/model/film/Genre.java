package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Genre {
    private final int id;
    private final String name;
}