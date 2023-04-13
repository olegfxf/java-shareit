package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getAll();
    Item save(Item item);
    Item update(Item item);
    Item getById(Long itemId);
    Item removeById(Long itemId);
}
