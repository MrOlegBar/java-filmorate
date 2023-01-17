package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.Collection;

@Service
@AllArgsConstructor
public class UserService {
    private UserDao userDbStorage;

    public User create(User user) throws UserNotFoundException {
        return userDbStorage.create(user);
    }

    public Collection<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public User getUserById(int userId) throws UserNotFoundException {
        return userDbStorage.getUserById(userId);
    }

    public User update(User user) throws UserNotFoundException {
        return userDbStorage.update(user);
    }
}