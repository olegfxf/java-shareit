package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.H2TestProfileJPAConfig;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.user.dto.UserDtoReq;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        ShareItApp.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Transactional
public class UserServiceTestJUnit {

    @Autowired
    private UserService userService;

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void updateCustomer() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("name@mail.ru");
        userService.save1(user);

        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setName("name1");
        userDtoReq.setEmail("name1@mail.ru");

        UserDtoRes actualUserDtoRes = userService.updateCustomer(1L, userDtoReq);

        assertEquals("name1", actualUserDtoRes.getName());
    }


}
