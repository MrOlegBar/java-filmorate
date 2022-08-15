package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<String, User> users = new HashMap<>();
    private final Set<Integer> ids = new HashSet<>();

    private int idGeneration(User user) {
        int id = user.getId();
        if (id == 1) {
            for (String email : users.keySet()) {
                User oldUser = users.get(email);
                ids.add(oldUser.getId());
            }
            while (ids.contains(id)) {
                id++;
            }
        }
        return id;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        user.setId(idGeneration(user));

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getEmail(), user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        for (String email : users.keySet()) {
            User oldUser = users.get(email);
            if ((user.getId() == oldUser.getId()) && (!user.getEmail().equals(oldUser.getEmail()))) {
                users.remove(email, oldUser);
            }
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getEmail(), user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }
}
