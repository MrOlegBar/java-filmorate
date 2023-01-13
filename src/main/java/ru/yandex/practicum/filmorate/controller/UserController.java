package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) throws UserNotFoundException, FriendNotFoundException {
        return userService.create(user);
    }

    @GetMapping(value = { "/users", "/users/{userId}"})
    public Object getUserS(@PathVariable(required = false) Integer userId) throws UserNotFoundException
            , FriendNotFoundException {
        if (userId == null) {
            return userService.getAllUsers();
        } else {
            return userService.getUserById(userId);
        }
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) throws UserNotFoundException, FriendNotFoundException {
        return userService.update(user);
    }
}