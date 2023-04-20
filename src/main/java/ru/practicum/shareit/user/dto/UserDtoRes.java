package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

@Setter
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDtoRes {
    Long id;
    String name;
    String email;

    public UserDtoRes(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
