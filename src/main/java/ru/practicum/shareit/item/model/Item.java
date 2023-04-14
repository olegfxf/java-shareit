package ru.practicum.shareit.item.model;

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
public class Item {
    long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    String request;
}
