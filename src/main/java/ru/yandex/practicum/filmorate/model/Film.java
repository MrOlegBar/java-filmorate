package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.FilmController;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Digits(integer = 2_147_483_647, fraction = 0) @Positive @NotNull
    private long duration;
    private Set<Long> likes;

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        //this.id = new FilmController().getIdGeneration();
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(String name, String description, LocalDate releaseDate, long duration, Set<Long> likes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
    }
}