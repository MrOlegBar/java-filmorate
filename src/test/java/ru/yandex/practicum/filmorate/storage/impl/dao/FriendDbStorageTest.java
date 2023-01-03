package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FriendDbStorageTest {
    @Autowired
    FriendDbStorage friendDbStorage;

    @Test
    void contextLoads() {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void addFriend() {
        User testUser = friendDbStorage.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        User testFriend = friendDbStorage.getUserDbStorage().create(User.builder()
                .login("testFriend login")
                .name("testFriend name")
                .email("friendemail@mail.ru")
                .birthday(LocalDate.parse("1967-10-25"))
                .build());

        User foundUser = friendDbStorage.addFriend(testUser.getId(), testFriend.getId());

        Map<Boolean, Set<Integer>> userFriends = testUser.getFriends();
        Set<Integer> falseUserFriends = userFriends.get(false);
        falseUserFriends.add(testFriend.getId());
        userFriends.put(false, falseUserFriends);
        testUser.setFriends(userFriends);

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);

        User foundFriend = friendDbStorage.addFriend(testFriend.getId(), testUser.getId());

        Map<Boolean, Set<Integer>> friendFriends = testFriend.getFriends();
        Set<Integer> trueFriendFriends = friendFriends.get(true);
        trueFriendFriends.add(testUser.getId());
        friendFriends.put(true, trueFriendFriends);
        testFriend.setFriends(friendFriends);

        assertNotNull(foundFriend);
        assertEquals(testFriend, foundFriend);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllFriends() {
        User testUser = friendDbStorage.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        User testFriend = friendDbStorage.getUserDbStorage().create(User.builder()
                .login("testFriend login")
                .name("testFriend name")
                .email("friendemail@mail.ru")
                .birthday(LocalDate.parse("1967-10-25"))
                .build());

        friendDbStorage.addFriend(testUser.getId(), testFriend.getId());

        Map<Boolean, Set<Integer>> friendFriends = testFriend.getFriends();
        Set<Integer> falseFriendFriends = friendFriends.get(false);
        falseFriendFriends.add(testUser.getId());
        friendFriends.put(false, falseFriendFriends);
        testFriend.setFriends(friendFriends);

        Collection<User> foundUserFriends = friendDbStorage.getAllFriends(testUser.getId());
        Collection<User> testUserFriends = new ArrayList<>();
        testUserFriends.add(testFriend);

        assertNotNull(foundUserFriends);
        assertEquals(testUserFriends, foundUserFriends);

        friendDbStorage.addFriend(testFriend.getId(), testUser.getId());

        Map<Boolean, Set<Integer>> userFriends = testUser.getFriends();
        Set<Integer> trueUserFriends = userFriends.get(true);
        trueUserFriends.add(testFriend.getId());
        userFriends.put(true, trueUserFriends);
        testUser.setFriends(userFriends);

        Collection<User> foundFriendFriends = friendDbStorage.getAllFriends(testFriend.getId());
        Collection<User> testFriendFriends = new ArrayList<>();
        testFriendFriends.add(testUser);

        assertNotNull(foundFriendFriends);
        assertEquals(testFriendFriends, foundFriendFriends);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getCorporateFriends() {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteFriend() {
    }
}