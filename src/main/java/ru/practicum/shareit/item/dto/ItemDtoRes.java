package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemDtoRes {
    @Positive
    long id;
    @NotNull
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    String name;
    String description;
    Boolean available;
    Long owner;
    String request;

    public ItemDtoRes(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.getAvailable();
        this.owner = item.getOwner();
        this.request = item.getRequest();
    }
}
