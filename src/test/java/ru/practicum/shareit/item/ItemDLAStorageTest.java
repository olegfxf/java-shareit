package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemDLAStorageTest {
    ItemDLAStorage itemDLAStorage;
    Item item = new Item();

    @Autowired
    public ItemDLAStorageTest(ItemDLAStorage itemDLAStorage) {
        this.itemDLAStorage = itemDLAStorage;
    }

    @BeforeEach
    void setUp() {
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequest("request");

        itemDLAStorage.removeALL();
    }

    @Test
    void shouldSave() {
        Item item1 = itemDLAStorage.save(item);
        assertEquals("name", itemDLAStorage.getById(item1.getId()).getName());
    }

    @Test
    void shouldGetAll() {
        itemDLAStorage.save(item);
        assertEquals(1, itemDLAStorage.getALL().size());
    }

    @Test
    void shouldSGetAllById() {
        itemDLAStorage.save(item);
        assertEquals(1, itemDLAStorage.getALL().size());
    }

    @Test
    void shouldGetById() {
        Item item1 = itemDLAStorage.save(item);
        assertEquals("name", itemDLAStorage.getById(item1.getId()).getName());
    }

    @Test
    void shouldRemoveById() {
        Item item1 = itemDLAStorage.save(item);
        itemDLAStorage.removeById(item1.getId());
        assertEquals(0, itemDLAStorage.getALL().size());
    }

    @Test
    void shouldUpdate() {
        Item item1 = itemDLAStorage.save(item);
        item.setName("name1");
        itemDLAStorage.update(item);
        assertEquals("name1", itemDLAStorage.getById(item1.getId()).getName());
    }

}