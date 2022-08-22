package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class UserService {
    private final Map<String, User> users = new TreeMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getEmail(), user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    public User update(User user) throws ValidationException {
        for (String email : users.keySet()) {
            User oldUser = users.get(email);
            if ((user.getId() == oldUser.getId()) && (!user.getEmail().equals(oldUser.getEmail()))) {
                users.remove(email, oldUser);
                users.put(user.getEmail(), user);
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

        users.put(user.getEmail(), user);

        log.debug("Пользователь: {} сохранен.", user);
        return user;
    }

    public int getIdGeneration() {
        return idGeneration();
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