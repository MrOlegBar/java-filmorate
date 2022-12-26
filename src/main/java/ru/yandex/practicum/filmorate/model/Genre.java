package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;

@Builder
@Data
public class Genre {
    @Digits(integer = 2_147_483_647, fraction = 0)
    private final int id;
    private final String name;
}