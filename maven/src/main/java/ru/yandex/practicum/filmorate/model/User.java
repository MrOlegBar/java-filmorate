package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@lombok.Data
public class User {
    @Digits(integer = 2_147_483_647, fraction = 0) @NotNull
    private final int id;
    @Email @NotNull
    private final String email;
    @NotBlank
    private final String login;
    @NotNull
    private final String name;
    @PastOrPresent @NotNull
    private final LocalDate birthday;
}
