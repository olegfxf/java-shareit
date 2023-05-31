package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoReq {
    @NotNull
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    String name;

    @NotNull
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    String email;

    public User toUser() {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

}