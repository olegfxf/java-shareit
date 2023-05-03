package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemServiceTest {
    ItemService itemService;


    @Autowired
    public ItemServiceTest(ItemRepository itemRepository, ItemService itemService) {
        this.itemService = itemService;
    }

    @Test
    void search() {
        Item item1 = new Item();
        item1.setName("Аккумуляторная дрель");
        item1.setDescription("Аккумуляторная дрель + аккумулятор");
        item1.setAvailable(true);
        item1.setOwnerId(1L);
        item1.setRequestId(10L);
        itemService.save(item1);

        Item item2 = new Item();
        item2.setName("Отвертка");
        item2.setDescription("Аккумуляторная отвертка");
        item2.setAvailable(true);
        item2.setOwnerId(3L);
        item2.setRequestId(10L);
        itemService.save(item2);

        assertEquals(2, itemService.search("аккУМУляторная").size());
    }
}