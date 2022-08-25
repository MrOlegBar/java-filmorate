package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    private final Map<String, User> users = new TreeMap<>();
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        return userStorage.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.update(user);
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
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
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidation(final ValidationException e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("error message", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleUserNotFound(final UserNotFoundException e) {
        return new ResponseEntity<>(
                Map.of("error message", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}