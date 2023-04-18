package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.abstracts.AbstractDLAStorage;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserDLAStorageTest {
    AbstractDLAStorage<User> userStorage;
    User user = new User();

    @Autowired
    public UserDLAStorageTest(AbstractDLAStorage<User> userStorage) {
        this.userStorage = userStorage;
    }

    @BeforeEach
    void setUp() {
        user.setName("name");
        user.setEmail("name@mail.ru");

        userStorage.removeALL();
    }

    @Test
    void shouldSave() {
        User user1 = userStorage.save(user);
        assertEquals("name", userStorage.getById(user1.getId()).getName());
    }

    @Test
    void shouldGetALL() {
        userStorage.save(user);
        assertEquals(1, userStorage.getALL().size());
    }

    @Test
    void shouldGetById() {
        User user1 = userStorage.save(user);
        assertEquals("name", userStorage.getById(user1.getId()).getName());
    }

    @Test
    void shouldUpdate() {
        User user1 = userStorage.save(user);
        user.setName("name1");
        userStorage.update(user);
        assertEquals("name1", userStorage.getById(user1.getId()).getName());
    }

    @Test
    void shouldRemoveById() {
        User user1 = userStorage.save(user);
        userStorage.removeById(user1.getId());
        assertEquals(0, userStorage.getALL().size());
    }

}