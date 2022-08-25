package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    @NotNull @Pattern(regexp = "\\S*") private String login;
    private String name;
    @NotNull @Email private String email;
    @NotNull @PastOrPresent private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public User(String login, String name, String email, LocalDate birthday, Set<Long> friends) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.friends = friends;
    }
}