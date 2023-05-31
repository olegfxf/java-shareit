package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDto {

    private Long id;

    private String name;

    @Email(message = "Email не корректен")

    private String email;
}
