package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public class ItemDLAStorage implements ItemStorage {
    static Map<Long, Item> mem = new HashMap<>();
    static Long id = 1L;

    public Item save(Item item) {
        getAll().stream().forEach(e -> {
            if (item.getName().equals(e.getName()))
                throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        });
        item.setId(id++);
        mem.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAll() {
        return mem.values().stream().collect(toList());
    }

    @Override
    public List<Item> getAll(Long id){
        List<Item> items = getAll().stream().filter(e->e.getOwner().equals(id)).collect(toList());
        return items;
    }

    @Override
    public Item getById(Long id) {
        return mem.get(id);
    }

    @Override
    public Item removeById(Long itemId) {
        return mem.remove(itemId);
    }


    @Override
    public Item update(Item item) {
        Long id = item.getId();
        if (getById(id) == null)
            throw new NotFoundException(String.valueOf(ExceptionMessages.NOT_FOUND_ID));
        mem.put(id, item);
        return item;
    }

}

