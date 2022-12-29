package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userDbStorage;

    public User create(User user) {
        return userDbStorage.create(user);
    }

    public User update(User user) throws UserNotFoundException {
        return userDbStorage.update(user);
    }

    public Collection<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public User getUserById(int userId) {
        return userDbStorage.getUserById(userId);
    }

}