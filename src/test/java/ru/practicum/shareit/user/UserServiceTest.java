package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.SneakyThrows;
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

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.*;

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

    @Mock
    ObjectMapper mapper;

    User expectedUser;

    Long userId = 1L;

    @BeforeEach
    void setUp() {
        expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("name");
        expectedUser.setEmail("name@mail.ru");
    }

    @SneakyThrows
    @Test
    void updateCustomer() {
//        User user = new User();
//        UserDtoRes userDtoRes = new UserDtoRes(user);
//
//        String json = "{\"name\":\"update\",\"email\":\"update@user.com\"}";
//        System.out.println(json);
//        final InputStream in = new ByteArrayInputStream(json.getBytes());
//        //final JsonMergePatch patch = mapper.readValue(in, JsonMergePatch.class);
////
////        JsonNode patched = patch.apply(mapper.convertValue(user, JsonNode.class));
////        final JsonMergePatch patch = mapper.readValue((DataInput) patched, JsonMergePatch.class);
//
//        JsonMergePatch mergePatch = Json.cJson.createMergePatch(JSONObject(
//                .add("work", JSONObject.createObjectBuilder()
//                        .add("title", "Senior Engineer"))
//                .build());
//
//        System.out.println(patch);
////        doReturn(expectedUser).
////                when(userRepository).save(expectedUser);
//                when(userRepository.findById(1l))
//                .thenReturn(Optional.of(user));
//
//                lenient().when(userService.applyPatchToUser(patch, user))
//                        .thenReturn(user);
//
////        when(userRepository.findById(1l))
////                .thenReturn(Optional.of(user));
//
//        when(userRepository.save(expectedUser))
//                .thenReturn(expectedUser);
//
//        userService.updateCustomer(1l, any());
//
////        User actualUser = UserMapper.toUserFromDtoRes(userService
////                .updateCustomer(userId, any(JsonMergePatch.class)));
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
    void update1_1() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(expectedUser));
        when(userRepository.save(expectedUser))
                .thenReturn(expectedUser);

        User actualUser = UserMapper.toUserFromDtoRes(userService.update1(expectedUser));

        assertEquals(expectedUser, actualUser);
    }

}
