package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User>  findAll();

    User create(User user);

    User update(User user);

    User getUserById(int userId);
}
