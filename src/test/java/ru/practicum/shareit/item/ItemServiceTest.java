package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.abstracts.AbstractStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemServiceTest {
    AbstractStorage<Item> itemStorage;
    ItemService itemService;

    @Autowired
    public ItemServiceTest(AbstractStorage<Item> itemStorage, ItemService itemService) {
        this.itemService = itemService;
        this.itemStorage = itemStorage;
    }

    @Test
    void search() {
        Item item1 = new Item();
        item1.setName("Аккумуляторная дрель");
        item1.setDescription("Аккумуляторная дрель + аккумулятор");
        item1.setAvailable(true);
        item1.setOwner(1L);
        item1.setRequest("request");
        itemStorage.save(item1);

        Item item2 = new Item();
        item2.setName("Отвертка");
        item2.setDescription("Аккумуляторная отвертка");
        item2.setAvailable(true);
        item2.setOwner(3L);
        item2.setRequest("request");
        itemStorage.save(item2);

        assertEquals(2, itemService.search("аккУМУляторная").size());
    }
}