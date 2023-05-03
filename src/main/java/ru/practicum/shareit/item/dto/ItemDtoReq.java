package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
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
    Long request;
}
