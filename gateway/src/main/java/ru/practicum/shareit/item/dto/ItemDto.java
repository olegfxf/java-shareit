package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @NotEmpty(groups = ItemDto.class)
    String name;

    @NotEmpty(groups = ItemDto.class)
    String description;

    @NotNull(groups = ItemDto.class)
    Boolean available;

    @Positive
    Long requestId;

}
