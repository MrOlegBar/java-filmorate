package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    @NotNull @Pattern(regexp = "\\S*") private String login;
    private String name;
    @NotNull @Email private String email;
    @NotNull @PastOrPresent private LocalDate birthday;
    private Map<String, List<Long>> friends = new HashMap<>();

    public User(String login, String name, String email, LocalDate birthday, Map<String, List<Long>> friends) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.friends = friends;
    }
}