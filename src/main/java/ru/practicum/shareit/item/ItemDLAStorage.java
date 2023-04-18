package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.abstracts.AbstractDLAStorage;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.HandlerMessages;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ItemDLAStorage extends AbstractDLAStorage<Item> {
    public Item save(Object obj) {
        mem.values().stream().forEach(e -> {
            if (((Item) obj).getName().equals(e.getName()))
                throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        });
        ((Item) obj).setId(id++);
        mem.put(((Item) obj).getId(), (Item) obj);
        return (Item) obj;
    }

    @Override
    public List<Item> getAll(Long id) {
        List<Item> items = getALL().stream().filter(e -> e.getOwner().equals(id)).collect(toList());
        return items;
    }

}

