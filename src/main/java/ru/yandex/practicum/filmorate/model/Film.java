package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import java.time.LocalDate;

@lombok.Data
public class Film {
    @Digits(integer = 2_147_483_647, fraction = 0) @Positive @NotNull
    private int id = 1;
    @NotBlank
    private String name;
    @Size(max = 200) @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Digits(integer = 2_147_483_647, fraction = 0) @Positive @NotNull
    private long duration;

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}