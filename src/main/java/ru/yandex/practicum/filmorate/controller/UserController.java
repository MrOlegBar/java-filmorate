package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<String, User> users = new TreeMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getEmail(), user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) throws ValidationException {
        for (String email : users.keySet()) {
            User oldUser = users.get(email);
            if ((user.getId() == oldUser.getId()) && (!user.getEmail().equals(oldUser.getEmail()))) {
                users.remove(email, oldUser);
                users.put(user.getEmail(), user);
            }

            if (user.getId() != oldUser.getId() || users.isEmpty() || user.getId() < 1){
                ValidationException e = new ValidationException("User с id = " + user.getId() + " не существует");
                log.debug("Валидация не пройдена", e);
                throw e;
            }
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getEmail(), user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    public int getIdGeneration() {
        return idGeneration();
    }

    private int idGeneration() {
        int id = 1;

        for (String email : users.keySet()) {
            User oldUser = users.get(email);
            int idUser = oldUser.getId();

            if (id == idUser) {
                id++;
            }
        }
        return id;
    }
}
