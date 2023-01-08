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

        User testCommonFriend = friendDbStorage.getUserDbStorage().create(User.builder()
                .login("commonFriend login")
                .name("commonFriend name")
                .email("commonfriendemail@mail.ru")
                .birthday(LocalDate.parse("1967-07-25"))
                .build());

        friendDbStorage.addFriend(testUser.getId(), testCommonFriend.getId());
        friendDbStorage.addFriend(testCommonFriend.getId(), testUser.getId());
        friendDbStorage.addFriend(testFriend.getId(), testCommonFriend.getId());
        friendDbStorage.addFriend(testCommonFriend.getId(), testFriend.getId());

        Collection<User> foundCommonFriends = friendDbStorage.getCorporateFriends(testUser.getId(),testFriend.getId());

        Map<Boolean, Set<Integer>> commonFriendFriends = testCommonFriend.getFriends();
        Set<Integer> trueCommonFriendFriends = commonFriendFriends.get(true);
        trueCommonFriendFriends.add(testUser.getId());
        trueCommonFriendFriends.add(testFriend.getId());
        commonFriendFriends.put(true, trueCommonFriendFriends);
        testCommonFriend.setFriends(commonFriendFriends);

        Collection<User> testCommonFriends = new ArrayList<>();
        testCommonFriends.add(testCommonFriend);

        assertNotNull(foundCommonFriends);
        assertEquals(testCommonFriends, foundCommonFriends);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteFriend() {
        User user = friendDbStorage.getUserDbStorage().create(User.builder()
                .login("login")
                .name("name")
                .email("email@mail.ru")
                .birthday(LocalDate.parse("1967-03-25"))
                .friends(new TreeMap<>())
                .build());

        User friend = friendDbStorage.getUserDbStorage().create(User.builder()
                .login("friend login")
                .name("friend name")
                .email("friendemail@mail.ru")
                .birthday(LocalDate.parse("1967-10-25"))
                .build());

        friendDbStorage.addFriend(user.getId(), friend.getId());
        friendDbStorage.addFriend(friend.getId(), user.getId());
        User testFriend = friendDbStorage.deleteFriend(friend.getId(), user.getId());

        Map<Boolean, Set<Integer>> friendFriends = friend.getFriends();
        Set<Integer> falseFriendFriends = friendFriends.get(false);
        falseFriendFriends.add(user.getId());
        friendFriends.put(false, falseFriendFriends);
        friend.setFriends(friendFriends);

        assertNotNull(testFriend);
        assertEquals(friend, testFriend);

        User testUser = friendDbStorage.deleteFriend(user.getId(), friend.getId());

        falseFriendFriends.clear();
        friendFriends.put(false, falseFriendFriends);
        user.setFriends(friendFriends);

        assertNotNull(testUser);
        assertEquals(user, testUser);
    }
}