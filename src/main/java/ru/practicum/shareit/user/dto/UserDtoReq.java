package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;
@Data
public class UserDtoReq {
    String name;
    String email;

    public static UserDtoReq toUserDtoReq(User user) {
        return new UserDtoReq(
                user.getName(),
                user.getEmail()
        );
    }

    public UserDtoReq() {
    }

    public UserDtoReq(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
