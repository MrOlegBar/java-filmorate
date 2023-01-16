package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class FriendController {
    private final FriendService friendService;
    @GetMapping(value = { "/users/{userId}/friends", "/users/{userId}/friends/common/{otherUserId}"})
    public Collection<User> getFriends(@PathVariable(required = false) Integer userId
            , @PathVariable(required = false) Integer otherUserId) throws UserNotFoundException {
        if (otherUserId == null) {
            return friendService.getAllFriends(userId);
        } else {
            return friendService.getCorporateFriends(userId, otherUserId);
        }
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public User putFriend(@PathVariable int userId, @PathVariable int friendId) throws UserNotFoundException {
        return friendService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable int userId, @PathVariable int friendId) throws UserNotFoundException {
        return friendService.deleteFriend(userId, friendId);
    }
}