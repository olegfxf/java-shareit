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
public class UserDtoReq {
    String name;

    String email;

    public User toUser() {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

}
