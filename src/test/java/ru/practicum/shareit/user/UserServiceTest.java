package ru.practicum.shareit.user;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Component
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    User expectedUser;

    Long userId = 1L;

    @BeforeEach
    void setUp() {
        expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("name");
        expectedUser.setEmail("name@mail.ru");
    }

    @Test
    void updateCustomer() {
//        doReturn(expectedUser).
//                when(userRepository).save(expectedUser);

//        when(userRepository.save(expectedUser))
//                .thenReturn(expectedUser);

//        User actualUser = UserMapper.toUserFromDtoRes(userService
//                .updateCustomer(userId, any(JsonMergePatch.class)));
    }

    @Test
    void save1() {
        when(userRepository.save(expectedUser))
                .thenReturn(expectedUser);

        User acutualUser = UserMapper.toUserFromDtoRes(userService.save1(expectedUser));

        assertEquals(expectedUser, acutualUser);
    }

    @Test
    void removeById1() {
        userRepository.deleteById(userId);

        verify(userRepository, times(1)).deleteById(eq(userId));
    }

    @Test
    void getAll1() {
        List<User> expectUsers = new ArrayList<>();
        when(userRepository.findAll())
                .thenReturn(expectUsers);

        List<User> actualUsers = userService.getAll1().stream()
                .map(e -> UserMapper.toUserFromDtoRes(e)).collect(Collectors.toList());

        assertEquals(expectUsers, actualUsers);
    }

    @Test
    void getById1() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(expectedUser));

        User actualUser = UserMapper.toUserFromDtoRes(userService.getById1(userId));

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void update1() {
        Long userId2 = 2L;
        lenient().when(userRepository.findById(userId2))
                .thenReturn(Optional.of(expectedUser));

        assertThrows(NotFoundException.class, () -> userService.update1(expectedUser));
    }

    @Test
    void update1_1(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(expectedUser));
        when(userRepository.save(expectedUser))
                .thenReturn(expectedUser);

        User actualUser = UserMapper.toUserFromDtoRes(userService.update1(expectedUser));

        assertEquals(expectedUser, actualUser);
    }

}
