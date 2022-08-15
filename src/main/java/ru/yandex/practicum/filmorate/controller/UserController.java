package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        try {
            users.put(user.getEmail(), user);
        } catch (Exception e) {
            log.debug("Валидация не пройдена", e);
        }
        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        try {
            users.put(user.getEmail(), user);
        } catch (Exception e) {
            log.debug("Валидация не пройдена", e);
        }
        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }
}
