package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoReq {
    long id;
    @NotNull
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    String name;
    @NotNull
    String description;
    @NotNull
    Boolean available;
    Long owner;
    String request;

    public Item toItem() {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner);
        item.setRequest(request);
        return item;
    }

}
