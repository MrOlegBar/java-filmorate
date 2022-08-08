package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@lombok.Data
public class Film {
    @Digits(integer = 2_147_483_647, fraction = 0) @NotNull
    private final int id;
    @NotBlank
    private final String name;
    @Size(max = 200) @NotNull
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Digits(integer = 2_147_483_647, fraction = 0) @NotNull @Positive
    private final long duration;
}