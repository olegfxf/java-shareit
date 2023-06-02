package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull
    @NotBlank(message = "Поле Имя пустое")
    private String name;

    @NotNull
    @Email(message = "Email не корректен")
    private String email;
}
