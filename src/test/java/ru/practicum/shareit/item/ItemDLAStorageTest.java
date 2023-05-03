package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemDLAStorageTest {
    ItemService itemService;
    Item item = new Item();

    @Autowired
    public ItemDLAStorageTest(ItemService itemService) {
        this.itemService = itemService;
    }

    @BeforeEach
    void setUp() {
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(1L);
        item.setRequestId(10L);

        itemService.removeALL();
    }

    @Test
    void shouldSave() {
        Item item1 = itemService.save(item);
        assertEquals("name", itemService.getById(item1.getId()).getName());
    }

    @Test
    void shouldGetAll() {
        itemService.save(item);
        assertEquals(1, itemService.getAll().size());
    }

    @Test
    void shouldSGetAllById() {
        itemService.save(item);
        assertEquals(1, itemService.getAll().size());
    }

    @Test
    void shouldGetById() {
        Item item1 = itemService.save(item);
        assertEquals("name", itemService.getById(item1.getId()).getName());
    }

    @Test
    void shouldRemoveById() {
        Item item1 = itemService.save(item);
        itemService.removeById(item1.getId());
        assertEquals(0, itemService.getAll().size());
    }

    @Test
    void shouldUpdate() {
        Item item1 = itemService.save(item);
        item.setName("name1");
        itemService.update(item);
        assertEquals("name1", itemService.getById(item1.getId()).getName());
    }

}