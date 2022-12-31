package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film {
    @Digits(integer = 2_147_483_647, fraction = 0)
    private int id;
    @NotBlank(message = "Название фильма отсутствует.")
    private String name;
    @Size(max = 200, message = "Количество символов в описании фильма > 200")
    @NotNull
    private String description;
    @NotNull(message = "Дата выхода фильма отсутствует.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Digits(integer = 32_767, fraction = 0)
    @Positive(message = "Задано не допустимое значение продолжительности фильма.")
    private short duration;
    private RatingMpa mpa;
    private Integer rate;
    private Set<Integer> likes;
    private List<Genre> genres;

    public Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("title", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration",  film.getDuration());
        values.put("rating_MPA_id",  film.getMpa().getId());
        values.put("rate",  film.getRate());
        return values;
    }
}