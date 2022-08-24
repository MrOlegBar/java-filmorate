package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.comparator.LikesFilmComparator;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

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
    LikesFilmComparator likesFilmComparator;
    @Autowired
    FilmStorage filmStorage;

    private Set<Long> likes = new TreeSet<>(likesFilmComparator);

    private final Set<Long> likes2 = new TreeSet<>((userId1, userId2) -> {
        long id1 = userId1;
        long id2 = userId2;

        Film film1 = filmStorage.getFilmById((int) id1);
        Film film2 = filmStorage.getFilmById((int) id2);

        return Integer.compare(film1.getLikes().size(), film2.getLikes().size());
    });

    public Film(String name, String description, LocalDate releaseDate, long duration, Set<Long> likes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
    }
}