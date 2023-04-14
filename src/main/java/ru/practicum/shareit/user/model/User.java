package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class User {
    Long id;
    String name;
    String email;

}
