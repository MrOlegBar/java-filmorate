package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.UserController;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    @NotNull @Pattern(regexp = "\\S*") private String login;
    private String name;
    @NotNull @Email private String email;
    @NotNull @PastOrPresent private LocalDate birthday;

    public User(String login, String name, String email, LocalDate birthday) {
        this.id = new UserController().getIdGeneration();
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}