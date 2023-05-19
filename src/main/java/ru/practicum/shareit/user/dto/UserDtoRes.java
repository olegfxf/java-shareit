package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
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
