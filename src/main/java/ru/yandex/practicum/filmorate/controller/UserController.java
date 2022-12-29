package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public User postUser(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody @Valid User user) throws UserNotFoundException {
        return userService.update(user);
    }

    @GetMapping(value = { "/users", "/users/{userId}"})
    //@ResponseStatus
    public Object getFilmS(@PathVariable(required = false) Integer userId) throws FilmNotFoundException {
        if (userId != null) {
            return userService.getUserById(userId);
        } else {
            return userService.getAllUsers();
        }
    }
}