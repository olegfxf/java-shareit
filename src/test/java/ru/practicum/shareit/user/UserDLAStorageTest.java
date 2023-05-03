package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserDLAStorageTest {

    UserService userService;
    User user = new User();

    @Autowired
    public UserDLAStorageTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        user.setName("name");
        user.setEmail("name@mail.ru");

        userService.removeALL();
    }

    @Test
    void shouldSave() {
        User user1 = userService.save(user);
        assertEquals("name", userService.getById(user1.getId()).getName());
    }

    @Test
    void shouldGetALL() {
        userService.save(user);
        assertEquals(1, userService.getAll().size());
    }

    @Test
    void shouldGetById() {
        User user1 = userService.save(user);
        assertEquals("name", userService.getById(user1.getId()).getName());
    }

    @Test
    void shouldUpdate() {
        User user1 = userService.save(user);
        user.setName("name1");
        userService.update(user);
        assertEquals("name1", userService.getById(user1.getId()).getName());
    }

    @Test
    void shouldRemoveById() {
        User user1 = userService.save(user);
        userService.removeById(user1.getId());
        assertEquals(0, userService.getAll().size());
    }

}