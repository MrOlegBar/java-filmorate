package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;

@Data
@Builder
public class RatingMpa {
    @Digits(integer = 2_147_483_647, fraction = 0)
    private int id;
    private String name;
}