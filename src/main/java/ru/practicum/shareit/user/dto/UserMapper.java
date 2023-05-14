package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDtoRes toUserDtoRes(User user) {
        return  new UserDtoRes(user);
    }
}
