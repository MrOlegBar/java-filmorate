package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FriendService {
    private final FriendDao friendDao;

    public Collection<User> getAllFriends(int userId) throws UserNotFoundException {
        return friendDao.getAllFriends(userId);
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherUserId) throws UserNotFoundException {
        return friendDao.getCommonFriends(userId, otherUserId);
    }

    public User addFriend(int userId, int friendId) throws UserNotFoundException {
        return friendDao.addFriend(userId, friendId);
    }

    public User deleteFriend(int userId, int friendId) throws UserNotFoundException {
        return friendDao.deleteFriend(userId, friendId);
    }
}