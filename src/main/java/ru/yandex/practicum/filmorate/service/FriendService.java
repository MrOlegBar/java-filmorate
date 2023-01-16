package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.dao.FriendDbStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FriendService {
    private final FriendDbStorage friendDbStorage;

    public Collection<User> getAllFriends(int userId) throws UserNotFoundException {
        return friendDbStorage.getAllFriends(userId);
    }

    public Collection<User> getCorporateFriends(int userId, int otherUserId) throws UserNotFoundException {
        return friendDbStorage.getCorporateFriends(userId, otherUserId);
    }

    public User addFriend(int userId, int friendId) throws UserNotFoundException {
        return friendDbStorage.addFriend(userId, friendId);
    }

    public User deleteFriend(int userId, int friendId) throws UserNotFoundException {
        return friendDbStorage.deleteFriend(userId, friendId);
    }
}