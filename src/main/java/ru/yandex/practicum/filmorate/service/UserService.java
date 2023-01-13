package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserService {
    private UserStorage userDbStorage;

    public User create(User user) throws UserNotFoundException, FriendNotFoundException {
        return userDbStorage.create(user);
    }

    public Collection<User> getAllUsers() throws UserNotFoundException, FriendNotFoundException {
        return userDbStorage.getAllUsers();
    }

    public User getUserById(int userId) throws UserNotFoundException, FriendNotFoundException {
        return userDbStorage.getUserById(userId);
    }

    public User update(User user) throws UserNotFoundException, FriendNotFoundException {
        return userDbStorage.update(user);
    }
}