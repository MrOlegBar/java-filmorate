package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<String, User> users = new TreeMap<>();
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.update(user);
    }
}