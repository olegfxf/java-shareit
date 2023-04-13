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
public class ItemService implements ItemStorage{
    ItemDao itemDao;

    @Autowired
    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public List<Item> getAll() {
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), itemDao.getALL());

        return itemDao.getALL();

    }

    public Item save(Item item) {
        log.debug(String.valueOf(LogMessages.ADD), item);

        return itemDao.save(item);
    }

    public Item update(Item item) {


        return itemDao.update(item);
    }

    public Item getById(Long itemId) {
        if (itemDao.getById(itemId).equals(null))
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        itemDao.getALL().stream().forEach(e -> System.out.println(e));
        //log.debug(String.valueOf(LogMessages.ADD), userId);
        log.debug(String.valueOf(LogMessages.GET), itemDao.getById(itemId));
        return itemDao.getById(itemId);
    }


    public Item removeById(Long itemId) {
        log.debug(String.valueOf(LogMessages.REMOVE), itemId);
        //System.out.println("/{userId} del");
        itemDao.removeById(itemId);
        return itemDao.getById(itemId);
    }
}
