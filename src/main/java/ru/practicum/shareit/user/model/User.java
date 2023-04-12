package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    Long id;

    @NotNull
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    private String name;

    @NotNull
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    private String email;

}
