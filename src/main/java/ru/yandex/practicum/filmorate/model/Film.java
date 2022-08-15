package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import java.time.LocalDate;

@lombok.Data
public class Film {
    @Digits(integer = 2_147_483_647, fraction = 0) @Positive @NotNull
    private final int id;
    @NotBlank
    private final String name;
    @Size(max = 200) @NotNull
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Digits(integer = 2_147_483_647, fraction = 0) @Positive @NotNull
    private final long duration;
}