package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemForRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForUser;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;
    private ItemRequestDtoForUser itemRequestDtoForUser;
    private ItemRequestDto itemRequestDto;

    private ItemRequest itemRequest;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        ItemForRequest itemForRequest = new ItemForRequest();
        itemForRequest.setId(1L);
        itemForRequest.setName("name");
        itemForRequest.setDescription("на дрель");
        itemForRequest.setAvailable(true);
        itemForRequest.setRequestId(1L);

        List<ItemForRequest> itemsForRequest = new ArrayList<>();
        itemsForRequest.add(itemForRequest);

        itemRequestDtoForUser = ItemRequestDtoForUser.builder()
                .id(1L)
                .description("Малогабаритная дрель")
                .created(LocalDateTime.of(2023, 2, 1, 0, 0))
                .items(itemsForRequest)
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requester(new User())
                .created(LocalDateTime.of(2023, 2, 1, 0, 0))
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .requester(new User())
                .created(LocalDateTime.of(2023, 2, 1, 0, 0))
                .build();
    }


    @SneakyThrows
    @Test
    void addItemRequest() {
        Long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User()));

        when(itemRequestService.addItemRequest(userId, itemRequest))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(ItemRequestMapper.toItemRequestDto(itemRequest)))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
        ;
    }

    @SneakyThrows
    @Test
    void getAllForOwner() {
        List<ItemRequestDtoForUser> expectedItems = List.of(itemRequestDtoForUser);

        Mockito.when(itemRequestService.getAllForOwner(anyLong()))
                .thenReturn(expectedItems);

        mvc.perform(get("/requests/")
                        .content(mapper.writeValueAsString(expectedItems))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDtoForUser.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDtoForUser.getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDtoForUser.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].items[0].name", is(itemRequestDtoForUser.getItems().get(0).getName())));

    }


    @SneakyThrows
    @Test
    void getAllForUser() {
        List<ItemRequestDtoForUser> expectedItems = List.of(itemRequestDtoForUser);

        Mockito.when(itemRequestService.getAllForUser(any(), anyInt(), anyLong()))
                .thenReturn(expectedItems);

        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(expectedItems))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDtoForUser.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDtoForUser.getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestDtoForUser.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].items[0].name", is(itemRequestDtoForUser.getItems().get(0).getName())));
    }


    @SneakyThrows
    @Test
    void getById() {
        Long requestId = 1L;
        Long userId = 1L;
        when(itemRequestService.getById(requestId, userId))
                .thenReturn(itemRequestDtoForUser);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .content(mapper.writeValueAsString(itemRequestDtoForUser))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoForUser.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoForUser.getDescription())))
                .andExpect(jsonPath("$.items.[0].description",
                        is(itemRequestDtoForUser.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestDtoForUser.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}