package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<String, User> users = new TreeMap<>();

    public void save(User user) {
        user.setId(idGeneration());
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
    public User update(User user) throws ValidationException {
        for (String email : users.keySet()) {
            User oldUser = users.get(email);
            if ((user.getId() == oldUser.getId()) && (!user.getEmail().equals(oldUser.getEmail()))) {
                users.remove(email, oldUser);
                save(user);
            }

            if (user.getId() != oldUser.getId() || users.isEmpty() || user.getId() < 1){
                ValidationException e = new ValidationException("User с id = " + user.getId() + " не существует");
                log.debug("Валидация не пройдена", e);
                throw e;
            }
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        save(user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    public User getUserById(int userId) {
        User user = null;
        for (User userFromMap : users.values()) {
            if (userId == userFromMap.getId()) {
                user = userFromMap;
            }
        }
        return user;
    }

    private int idGeneration() {
        int id = 1;

        for (String email : users.keySet()) {
            User oldUser = users.get(email);
            int idUser = oldUser.getId();

            if (id == idUser) {
                id++;
            }
        }
        return id;
    }
}