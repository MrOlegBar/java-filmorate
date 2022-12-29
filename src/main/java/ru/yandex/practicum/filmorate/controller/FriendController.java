package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;

@RestController
@AllArgsConstructor
public class FriendController {
    private final FriendService friendService;
    @PutMapping("/users/{userId}/friends/{friendId}")
    public User putFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId)
            throws UserNotFoundException {
        return friendService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable("userId") int userId
            , @PathVariable("friendId") int friendId) throws UserNotFoundException {
        return friendService.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public List<User> getFriends(@PathVariable("userId") int userId) throws UserNotFoundException {
        return friendService.getFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherUserId}")
    public List<User> getCorporateFriends(@PathVariable("userId") int userId
            , @PathVariable("otherUserId") int otherUserId) throws UserNotFoundException {
        return friendService.getCorporateFriends(userId, otherUserId);
    }
}
