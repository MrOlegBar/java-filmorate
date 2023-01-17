package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank(message = "Название фильма отсутствует или представлено пустым символом.")
    private String name;
    @Size(max = 200, message = "Количество символов в описании фильма > 200.")
    @NotNull(message = "Описание отсутствует.")
    private String description;
    @NotNull(message = "Дата выхода фильма отсутствует.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Задано не допустимое значение продолжительности фильма.")
    private short duration;
    private RatingMpa mpa;
    private Integer rate;
    private Set<Integer> likes;
    private List<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("title", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration",  duration);
        values.put("rating_MPA_id",  mpa.getId());
        values.put("rate",  (rate == null) ? 0 : rate);
        return values;
    }
}