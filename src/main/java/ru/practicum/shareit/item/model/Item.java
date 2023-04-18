package ru.practicum.shareit.item.model;

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
public class Item extends AbstractModel {
    String name;
    String description;
    Boolean available;
    Long owner;
    String request;
}
