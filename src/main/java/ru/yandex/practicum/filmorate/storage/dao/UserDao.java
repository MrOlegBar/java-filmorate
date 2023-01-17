package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

public interface UserDao {
    User create(User user);

    Collection<User> getAllUsers();

    User getUserById(int userId);

    User update(User user);
}
