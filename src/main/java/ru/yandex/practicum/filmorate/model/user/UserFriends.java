package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;

import javax.validation.constraints.Digits;

@Builder
public class UserFriends {
    @Digits(integer = 2_147_483_647, fraction = 0)
    private final int userId;
    @Digits(integer = 2_147_483_647, fraction = 0)
    private final int friendId;
}
