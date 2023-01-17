package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageTest {
    @Autowired
    @Qualifier("userDbStorage")
    UserDao userDbStorage;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void create() {
        Map<Boolean, Set<Integer>> friends = new TreeMap<>();
        friends.put(false, new TreeSet<>());
        friends.put(true, new TreeSet<>());

        User testUser = User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .build();

        User foundUser = userDbStorage.create(testUser);

        testUser.setId(foundUser.getId());
        testUser.setFriends(friends);

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllUsers() {
        Map<Boolean, Set<Integer>> friends = new TreeMap<>();
        friends.put(false, new TreeSet<>());
        friends.put(true, new TreeSet<>());

        User testUser = User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .build();

        User foundUser = userDbStorage.create(testUser);

        testUser.setId(foundUser.getId());
        testUser.setFriends(friends);

        Collection<User> testUsers = new ArrayList<>();
        testUsers.add(testUser);

        Collection<User> foundUsers = userDbStorage.getAllUsers();

        assertNotNull(foundUsers);
        assertEquals(testUsers, foundUsers);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getUserById() {
        Map<Boolean, Set<Integer>> friends = new TreeMap<>();
        friends.put(false, new TreeSet<>());
        friends.put(true, new TreeSet<>());

        User user = User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .build();

        User testUser = userDbStorage.create(user);

        user.setId(testUser.getId());
        user.setFriends(friends);

        User foundUser = userDbStorage.getUserById(testUser.getId());

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void update() {
        User user = User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .build();

        User createUser = userDbStorage.create(user);

        User foundUser = userDbStorage.update(User.builder()
                .id(createUser.getId())
                .login("new login")
                .name("new name")
                .email("newemail@mail.ru")
                .birthday(LocalDate.parse("1967-10-25"))
                .build());

        User testUser = userDbStorage.getUserById(foundUser.getId());

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }
}