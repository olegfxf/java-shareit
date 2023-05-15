package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDtoRes toUserDtoRes(User user) {
        return new UserDtoRes(user);
    }

    public static User toUserFromDtoRes(UserDtoRes userDtoRes) {
        User user = new User();
        user.setId(userDtoRes.getId());
        user.setName(userDtoRes.getName());
        user.setEmail(userDtoRes.getEmail());
        return user;
    }

}
