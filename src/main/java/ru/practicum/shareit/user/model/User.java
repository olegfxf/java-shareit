package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstracts.AbstractModel;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class User extends AbstractModel {
    String name;
    String email;
}
