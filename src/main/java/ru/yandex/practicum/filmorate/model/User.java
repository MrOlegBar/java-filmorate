package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.controller.UserController;

import javax.validation.constraints.*;
import java.time.LocalDate;

@lombok.Data
public class User {
    private final int id;
    @Pattern(regexp = "\\S*") @NotNull
    private String login;
    private String name;
    @Email @NotNull
    private String email;
    @PastOrPresent @NotNull
    private LocalDate birthday;

    User(String login, String name, String email, LocalDate birthday) {
        this.id = new UserController().getIdGeneration();
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}