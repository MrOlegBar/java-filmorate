package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import java.time.LocalDate;

@lombok.Data
public class User {
    @Digits(integer = 2_147_483_647, fraction = 0) @Positive @NotNull
    private int id = 1;
    @NotBlank
    private String login;
    @NotNull
    private String name;
    @Email @NotNull
    private String email;
    @PastOrPresent @NotNull
    private LocalDate birthday;

    User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}