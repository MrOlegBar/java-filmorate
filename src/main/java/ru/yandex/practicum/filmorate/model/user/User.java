package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class User {
    private int id;
    @Pattern(regexp = "\\S*", message = "Логин пользователя не должен содержать пустой символ.") private String login;
    private String name;
    @NotNull @Email(message = "Email не соответствует формату электронной почты.") private String email;
    @NotNull @PastOrPresent(message = "Дата рождения еще не существует.")
    private LocalDate birthday;
    private Map<Boolean, Set<Integer>> friends;

    public Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("birthday",  user.getBirthday());
        return values;
    }
}