package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<String, User> users = new TreeMap<>();
    private static Integer globalId = 0;

    public void save(User user) {
        user.setId(getNextId());
        users.put(user.getEmail(), user);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        save(user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    @Override
    public User update(User user) throws UserNotFoundException {
        for (String email : users.keySet()) {
            User oldUser = users.get(email);
            if (user.getId() == oldUser.getId()) {
                users.remove(email, oldUser);
                users.put(user.getEmail(), user);
            } else {
                UserNotFoundException e = new UserNotFoundException("User с id = " + user.getId() + " не существует");
                log.debug("Валидация не пройдена", e);
                throw e;
            }
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        log.debug("Пользователь: {} сохранен.", user);
        return users.get(user.getEmail());
    }

    public User getUserById(int userId) throws UserNotFoundException {
        User user = null;
        for (User userFromMap : users.values()) {
            if (userId == userFromMap.getId()) {
                user = userFromMap;
            }
        }

        if (user == null) {
            UserNotFoundException e = new UserNotFoundException("User с id = " + userId + " не существует");
            log.debug("Валидация не пройдена", e);
            throw e;
        }
        return user;
    }

    private static Integer getNextId() {
        return ++globalId;
    }
}