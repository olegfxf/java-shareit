package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;

import java.util.List;

@Service
@Slf4j
public class ItemService {
    ItemStorage itemStorage;

    @Autowired
    public ItemService(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }


    public Item save(Item item) {
        Item item1 = itemStorage.save(item);
        log.debug(String.valueOf(LogMessages.ADD), item);
        return item1;
    }

    public List<Item> getAll(Long id) {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), itemStorage.getAll());
        return itemStorage.getAll(id);

    }

    public Item getById(Long itemId) {
        if (itemStorage.getById(itemId).equals(null))
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        itemStorage.getAll().stream().forEach(e -> System.out.println(e));
        //log.debug(String.valueOf(LogMessages.ADD), userId);
        log.debug(String.valueOf(LogMessages.GET), itemStorage.getById(itemId));
        return itemStorage.getById(itemId);
    }


    public Item update(Item item) {
        Item item1 = itemStorage.update(item);
        log.debug(String.valueOf(LogMessages.TRY_PATCH), item1);
        return item1;
    }


    public Item removeById(Long itemId) {
        log.debug(String.valueOf(LogMessages.REMOVE), itemId);
        //System.out.println("/{userId} del");
        itemStorage.removeById(itemId);
        return itemStorage.getById(itemId);
    }
}
