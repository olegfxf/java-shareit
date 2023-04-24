package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.constraints.AllForEmail;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoReq {
    @NotNull
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    String name;

    @NotNull
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    @AllForEmail(message = "Такой email уже существует")
    String email;

    public User toUser() {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

}
