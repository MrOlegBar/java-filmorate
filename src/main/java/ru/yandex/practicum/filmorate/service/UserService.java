package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int userId, int friendId) throws UserNotFoundException {
        User user = userStorage.getUserById(userId);
        if (user != null) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                Set<Long> friendsUser = user.getFriends();
                Set<Long> friendsFriend = friend.getFriends();
                friendsUser.add((long) friendId);
                friendsFriend.add((long) userId);
                return user;
            } else {
                throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", friendId));
            }
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    public User deleteFriend(int userId, int friendId) throws UserNotFoundException {
        User user = userStorage.getUserById(userId);
        if (user != null) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                Set<Long> friendsUser = user.getFriends();
                Set<Long> friendsFriend = friend.getFriends();
                friendsUser.remove((long) friendId);
                friendsFriend.remove((long) userId);
                return user;
            } else {
                throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", friendId));
            }
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    public List<User> getFriends(int userId) throws UserNotFoundException {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        if (user != null) {
                Set<Long> userFriends = user.getFriends();
                for (long friendId : userFriends) {
                    User friend = userStorage.getUserById((int) friendId);
                    friends.add(friend);
                }
                return friends;
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    public List<User> getCorporateFriends(int userId, int otherUserId) throws UserNotFoundException {
        List<User> corporateFriends = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        if (user != null) {
            User otherUser = userStorage.getUserById(otherUserId);
            if (otherUser != null) {
                Set<Long> userFriends = user.getFriends();
                Set<Long> otherUserFriends = otherUser.getFriends();
                for (long userFriendId : userFriends) {
                    if (otherUserFriends.contains(userFriendId)) {
                        User friend = userStorage.getUserById((int) userFriendId);
                        corporateFriends.add(friend);
                    }
                }
                return corporateFriends;
            } else {
                throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", otherUserId));
            }
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }
}