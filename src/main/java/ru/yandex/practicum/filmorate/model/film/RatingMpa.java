package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingMpa {
    private int id;
    private String name;
}