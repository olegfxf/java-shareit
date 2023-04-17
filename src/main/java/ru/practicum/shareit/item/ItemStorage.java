package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item save(Item item);

    List<Item> getAll();

    List<Item> getAll(Long id);

    Item getById(Long itemId);

    Item update(Item item);

    Item removeById(Long itemId);

    void removeAll();
}
