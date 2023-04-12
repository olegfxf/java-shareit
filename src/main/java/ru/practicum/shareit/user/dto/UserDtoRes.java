package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDtoRes {
    Long id;
    String name;
    String email;

    public static UserDtoRes toUserDtoRes(User user) {
        return new UserDtoRes(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserDtoRes(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
