package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.H2TestProfileJPAConfig;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDtoReq;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {
        ShareItApp.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    User user;
    UserDtoReq userDtoReq;

    UserDtoRes userDtoRes = new UserDtoRes();

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("name@mail.ru");
        userService.save1(user);

        userDtoReq = new UserDtoReq();
        userDtoReq.setName("name1");
        userDtoReq.setEmail("name1@mail.ru");

    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void updateCustomer() {
        UserDtoRes actualUserDtoRes = userService.updateCustomer(1L, userDtoReq);

        assertEquals("name1", actualUserDtoRes.getName());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void updateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> userService
                .updateCustomer(5L, userDtoReq));
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void save1() {
        User actualUser = userService.save(user);
        assertEquals("name", actualUser.getName());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void removeById1() {
        User actualUser = userService.save(user);
        userDtoRes = userService.removeById1(actualUser.getId());

        assertEquals(0, userRepository.findAll().size());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getAll1() {
        User actualUser = userService.save(user);
        List<UserDtoRes> userDtoResList = userService.getAll1();

        assertEquals(1, userDtoResList.size());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getById1() {
        User actualUser = userService.save(user);
        UserDtoRes userDtoRes1 = userService.getById1(actualUser.getId());

        assertEquals(actualUser.getId(), userDtoRes1.getId());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void update1() {
        User actualUser = userService.save(user);
        user.setName("nameNew");
        UserDtoRes updateUser = userService.update1(user);

        assertEquals("nameNew", updateUser.getName());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void update1NotFound() {
        user.setId(5L);
        assertThrows(NotFoundException.class, () -> userService
                .update1(user));
    }


}
