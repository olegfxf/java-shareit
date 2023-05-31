package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private UserDtoRes userDtoRes;

    private User user = new User();

    @BeforeEach
    void setUp() {
        user.setId(1L);
        user.setName("name");
        user.setEmail("name@mail.ru");
        userDtoRes = new UserDtoRes(user);
    }

    @SneakyThrows
    @Test
    void save() {
        when(userService.save1(any()))
                .thenReturn(userDtoRes);

        mvc.perform(post("/users/")
                        .content(mapper.writeValueAsString(userDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoRes.getName())))
                .andExpect(jsonPath("$.email", is(userDtoRes.getEmail())));
    }

    @SneakyThrows
    @Test
    void getAll() {
        List<UserDtoRes> expectedUsers = List.of(userDtoRes);

        Mockito.when(userService.getAll1())
                .thenReturn(expectedUsers);

        mvc.perform(get("/users")
                        .content(mapper.writeValueAsString(userDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDtoRes.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDtoRes.getEmail())));
    }

    @SneakyThrows
    @Test
    void getById() {
        Long userId = 1L;
        when(userService.getById1(userId))
                .thenReturn(userDtoRes);

        mvc.perform(get("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoRes.getName())))
                .andExpect(jsonPath("$.email", is(userDtoRes.getEmail())));
    }

    @SneakyThrows
    @Test
    void update() {
        when(userService.update1(any()))
                .thenReturn(userDtoRes);

        mvc.perform(put("/users", user)
                        .content(mapper.writeValueAsString(userDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoRes.getName())))
                .andExpect(jsonPath("$.email", is(userDtoRes.getEmail())));
    }

    @Test
    void removeById() throws Exception {
        Long userId = 1L;
        when(userService.removeById1(userId))
                .thenReturn(userDtoRes);

        mvc.perform(delete("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoRes.getName())))
                .andExpect(jsonPath("$.email", is(userDtoRes.getEmail())));
    }

    @SneakyThrows
    @Test
    void updateCustomer() {
        Long userId = 1L;
        when(userService.updateCustomer(anyLong(), any()))
                .thenReturn(userDtoRes);

        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoRes.getName())))
                .andExpect(jsonPath("$.email", is(userDtoRes.getEmail())));
    }
}