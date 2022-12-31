package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    Collection<User> getAllUsers();

    User getUserById(int userId);

    User update(User user);
}
