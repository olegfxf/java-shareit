package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoReq {
    @NotNull
    @NotEmpty(message = ExceptionMessages.EMPTY_NAME)
    String name;

    @NotNull
    String description;

    @NotNull
    Boolean available;

    @Positive
    Long ownerId;

    @Positive
    Long requestId;
}
