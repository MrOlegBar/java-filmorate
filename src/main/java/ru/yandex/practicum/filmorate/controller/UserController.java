package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody @Valid User user) throws UserNotFoundException {
        return userService.update(user);
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable("userId") int userId) {
        return userService.getUserById(userId);
    }

    /*@PutMapping("/users/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId)
            throws UserNotFoundException {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable("userId") int userId
            , @PathVariable("friendId") int friendId) throws UserNotFoundException {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public List<User> getFriends(@PathVariable("userId") int userId) throws UserNotFoundException {
        return userService.getFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherUserId}")
    public List<User> getCorporateFriends(@PathVariable("userId") int userId
            , @PathVariable("otherUserId") int otherUserId) throws UserNotFoundException {
        return userService.getCorporateFriends(userId, otherUserId);
    }*/
}