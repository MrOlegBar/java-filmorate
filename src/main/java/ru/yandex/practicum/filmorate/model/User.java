package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.controller.UserController;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
public class User {
    private int id;
    @Pattern(regexp = "\\S*") @NotNull
    private String login;
    private String name;
    @Email @NotNull
    private String email;
    @PastOrPresent @NotNull
    private LocalDate birthday;

    public User(String login, String name, String email, LocalDate birthday) {
        this.id = new UserController().getIdGeneration();
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}