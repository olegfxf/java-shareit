package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Positive;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    String name;

    String description;

    Boolean available;

    @Positive
    Long requestId;

}
