package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;

@RestController
@AllArgsConstructor
public class FriendController {
    private final FriendService friendService;
    @GetMapping(value = { "/users/{userId}/friends", "/users/{userId}/friends/common/{otherUserId}"})
    public List<User> getFriends(@PathVariable(required = false) Integer userId
            , @PathVariable(required = false) Integer otherUserId) throws UserNotFoundException
            , FriendNotFoundException {
        if (otherUserId == null) {
            return friendService.getFriends(userId);
        } else {
            return friendService.getCorporateFriends(userId, otherUserId);
        }
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public User putFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId)
            throws UserNotFoundException {
        return friendService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable("userId") int userId
            , @PathVariable("friendId") int friendId) throws UserNotFoundException, FriendNotFoundException {
        return friendService.deleteFriend(userId, friendId);
    }
}